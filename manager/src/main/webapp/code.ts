"use strict";

function displayTaskTable(): void {
    const table: HTMLTableElement = document.createElement('table');
    table.id = "taskTable";
    // the header row
    const headerRow: HTMLTableRowElement = table.createTHead().insertRow(0);
    // timestamp
    const timeStampHeader: HTMLTableCellElement = document.createElement('th');
    timeStampHeader.textContent = 'Timestamp';
    headerRow.appendChild(timeStampHeader);
    // ID
    const idHeader: HTMLTableCellElement = document.createElement('th');
    idHeader.textContent = 'ID';
    headerRow.appendChild(idHeader);
    // description
    const descriptionHeader: HTMLTableCellElement = document.createElement('th');
    descriptionHeader.textContent = 'Description';
    headerRow.appendChild(descriptionHeader);
    // edit button column
    const editHeader: HTMLTableCellElement = document.createElement('th');
    headerRow.appendChild(editHeader);
    // delete button column
    const deleteHeader: HTMLTableCellElement = document.createElement('th');
    headerRow.appendChild(deleteHeader);
    document.body.appendChild(table);
}

displayTaskTable();

function refreshTaskTable(): void {
    fetch('http://localhost:8080/api/tasks/')
        .then((response: Response) => response.json())
        .then((data: { uuid: string; clientTimeStamp: string; humanId: string; humanDescription: string; }[]) => {
            const table: HTMLElement | null = document.getElementById('taskTable');
            if (!table) {
                throw new Error('Task table not found');
            }
            if (!(table instanceof HTMLTableElement)) {
                throw new Error('Task table is not an HTMLTableElement');
            }
            // Remove all rows except for the header row
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            const fragment = document.createDocumentFragment();
            data.forEach((task: { uuid: string; clientTimeStamp: string; humanId: string; humanDescription: string; }) => {
                const row: HTMLTableRowElement = document.createElement('tr');
                // timestamp
                const timeStampCell: HTMLTableCellElement = document.createElement('td');
                timeStampCell.textContent = task.clientTimeStamp;
                row.appendChild(timeStampCell);
                // ID
                const idCell: HTMLTableCellElement = document.createElement('td');
                idCell.textContent = task.humanId;
                row.appendChild(idCell);
                // description
                const descriptionCell: HTMLTableCellElement = document.createElement('td');
                descriptionCell.textContent = task.humanDescription;
                row.appendChild(descriptionCell);
                // edit button
                const editCell: HTMLTableCellElement = document.createElement('td');
                const editButton: HTMLButtonElement = document.createElement('button');
                editButton.textContent = '✎';
                editButton.addEventListener('click', () => openTaskDialog(task.uuid));
                editCell.appendChild(editButton);
                row.appendChild(editCell);
                // delete button
                const deleteCell: HTMLTableCellElement = document.createElement('td');
                const deleteButton: HTMLButtonElement = document.createElement('button');
                deleteButton.textContent = '🗑';
                deleteButton.addEventListener('click', () => deleteTask(task.uuid));
                deleteCell.appendChild(deleteButton);
                row.appendChild(deleteCell);
                // append the row
                fragment.appendChild(row);
            });
            table.appendChild(fragment);

        })
        .catch(error => console.error('Error fetching tasks:', error));
}

function getUUID(): string {
    return URL.createObjectURL(new Blob()).slice(-36);
}

function getTimestamp(): string {
    const now: Date = new Date();
    const year: number = now.getFullYear();
    const month: string = (now.getMonth() + 1).toString().padStart(2, '0');
    const day: string = now.getDate().toString().padStart(2, '0');
    const hours: string = now.getHours().toString().padStart(2, '0');
    const minutes: string = now.getMinutes().toString().padStart(2, '0');
    const seconds: string = now.getSeconds().toString().padStart(2, '0');
    const milliseconds: string = now.getMilliseconds().toString().padStart(3, '0');
    const offset: number = -now.getTimezoneOffset() / 60;
    const offsetSign: string = (offset >= 0) ? '+' : '-';
    const offsetHours: string = Math.abs(Math.floor(offset)).toString().padStart(2, '0');
    const offsetMinutes: string = Math.abs(offset % 1 * 60).toString().padStart(2, '0');
    const timezone: string = Intl.DateTimeFormat().resolvedOptions().timeZone;

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}${offsetSign}${offsetHours}:${offsetMinutes}[${timezone}]`;
}

function deleteTask(taskId: string): void {
    fetch(`http://localhost:8080/api/tasks/${taskId}`, {
        method: 'DELETE'
    })
        .then((response: Response) => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to delete task: ' + response.status);
            }
        })
        .then((message: string) => {
            console.log('Task deleted:', message);
            refreshTaskTable();
        })
        .catch((error: string) => console.error('Error deleting task: ', error));
}

function openTaskDialog(taskId: string | null = null): void {
    const dialog: HTMLDialogElement = document.createElement('dialog');
    dialog.id = 'taskDialog';
    dialog.setAttribute('open', '');

    const title: HTMLHeadingElement = document.createElement('h3');
    title.textContent = taskId ? 'Edit Task' : 'Create Task';
    dialog.appendChild(title);

    const form: HTMLFormElement = document.createElement('form');
    form.id = 'taskForm';
    form.method = 'dialog';

    const idLabel: HTMLLabelElement = document.createElement('label');
    idLabel.textContent = 'ID';
    form.appendChild(idLabel);

    const idInput: HTMLInputElement = document.createElement('input');
    idInput.type = 'text';
    idInput.name = 'humanId';
    idInput.required = true;
    form.appendChild(idInput);

    const descLabel: HTMLLabelElement = document.createElement('label');
    descLabel.textContent = 'Description';
    form.appendChild(descLabel);

    const descInput: HTMLTextAreaElement = document.createElement('textarea');
    descInput.name = 'humanDescription';
    descInput.rows = 8;
    descInput.required = true;
    form.appendChild(descInput);

    const submitButton: HTMLButtonElement = document.createElement('button');
    submitButton.type = 'submit';
    submitButton.textContent = taskId ? 'Update Task' : 'Create Task';
    form.appendChild(submitButton);

    // Prepopulate form if taskId is provided
    if (taskId) {
        fetch(`http://localhost:8080/api/tasks/${taskId}`)
            .then((response: Response) => response.json())
            .then((task: { humanId: string; humanDescription: string }) => {
                idInput.value = task.humanId;
                descInput.value = task.humanDescription;
            })
            .catch((error: string) => console.error('Error fetching task:', error));
    }

    form.onsubmit = (event) => {
        event.preventDefault();
        const taskData = {
            uuid: taskId || getUUID(),
            clientTimeStamp: getTimestamp(),
            humanId: idInput.value,
            humanDescription: descInput.value
        };

        const method = taskId ? 'PUT' : 'POST';
        const url = taskId ? `http://localhost:8080/api/tasks/${taskId}` : 'http://localhost:8080/api/tasks/';

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData)
        })
            .then((response: Response) => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error(`Failed to ${taskId ? 'update' : 'create'} task: ` + response.status);
                }
            })
            .then((message: string) => {
                console.log(`Task ${taskId ? 'updated' : 'created'}:`, message);
                refreshTaskTable();
                dialog.close();
            })
            .catch((error: string) => console.error(`Error ${taskId ? 'updating' : 'creating'} task: `, error));
    };

    const cancelButton = document.createElement('button');
    cancelButton.type = 'button';
    cancelButton.textContent = 'Cancel';
    cancelButton.onclick = () => dialog.close();
    form.appendChild(cancelButton);

    dialog.appendChild(form);
    document.body.appendChild(dialog);
}
