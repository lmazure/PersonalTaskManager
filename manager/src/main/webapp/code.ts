fetch('http://localhost:8080/api/tasks/')
    .then(response => response.json())
    .then(data => {
        const table = document.createElement('table');
        table.innerHTML = `
      <tr>
        <th>UUID</th>
        <th>Timestamp</th>
        <th>ID</th>
        <th>Description</th>
      </tr>
    `;
        data.forEach((task: { uuid: any; clientTimeStamp: any; humanId: any; humanDescription: any; }) => {
            const row = table.insertRow(-1);
            row.innerHTML = `
        <td>${task.uuid}</td>
        <td>${task.clientTimeStamp}</td>
        <td>${task.humanId}</td>
        <td>${task.humanDescription}</td>
      `;
        });
        document.body.appendChild(table);
    })
    .catch(error => console.error('Error fetching tasks:', error));

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
    const offsetSign: string = offset >= 0 ? '+' : '-';
    const offsetHours: string = Math.abs(Math.floor(offset)).toString().padStart(2, '0');
    const offsetMinutes: string = Math.abs(offset % 1 * 60).toString().padStart(2, '0');
    const timezone: string = Intl.DateTimeFormat().resolvedOptions().timeZone;

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}${offsetSign}${offsetHours}:${offsetMinutes}[${timezone}]`;
}

console.log(getTimestamp());

function openTaskDialog() {
    const humanId: string|null = prompt('Enter human ID:');
    const humanDescription: string|null = prompt('Enter human description:');

    const taskData = {
        uuid: getUUID(),
        clientTimeStamp: getTimestamp(),
        humanId: humanId,
        humanDescription: humanDescription
    };

    fetch('http://localhost:8080/api/tasks/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(taskData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to create task: ' + response.status + ' ' + response.text());
            }
        })
        .then(data => {
            console.log('Task created:', data);
            // Optionally, you can refresh the task list or update the UI here.
        })
        .catch(error => console.error('Error creating task: ', error));
}

// Call this function when you need to open the task dialog, for example:
// openTaskDialog();

openTaskDialog();
