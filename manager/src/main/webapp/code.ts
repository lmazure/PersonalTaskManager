"use strict";

function displayTaskTable(): void {
    const table: HTMLTableElement = document.createElement('table');
    table.id = "taskTable";
    table.innerHTML = `<tr><th>Timestamp</th><th>ID</th><th>Description</th></tr>`;
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
            data.forEach((task: { uuid: string; clientTimeStamp: string; humanId: string; humanDescription: string; }) => {
                const row = table.insertRow(-1);
                row.innerHTML = `<td>${task.clientTimeStamp}</td><td>${task.humanId}</td><td>${task.humanDescription}</td>`;
            });
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

function openTaskDialog(): void {
    const humanId: string | null = prompt('Enter human ID:');
    if (humanId === null) {
        return;
    }
    const humanDescription: string | null = prompt('Enter human description:');
    if (humanDescription === null) {
        return;
    }

    const taskData: { uuid: string; clientTimeStamp: string; humanId: string; humanDescription: string; } = {
        uuid: getUUID(),
        clientTimeStamp: getTimestamp(),
        humanId: humanId,
        humanDescription: humanDescription
    };

    fetch('http://localhost:8080/api/tasks/', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(taskData)
    })
        .then((response: Response) => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to create task: ' + response.status);
            }
        })
        .then((message: string) => {
            console.log('Task created:', message);
            refreshTaskTable();
        })
        .catch((error: string) => console.error('Error creating task: ', error));

}
