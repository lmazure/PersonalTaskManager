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

function formatCurrentDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    const day = now.getDate().toString().padStart(2, '0');
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    const seconds = now.getSeconds().toString().padStart(2, '0');
    const milliseconds = now.getMilliseconds().toString().padStart(3, '0');
    const offset = -now.getTimezoneOffset() / 60;
    const offsetSign = offset >= 0 ? '+' : '-';
    const offsetHours = Math.abs(Math.floor(offset)).toString().padStart(2, '0');
    const offsetMinutes = Math.abs(offset % 1 * 60).toString().padStart(2, '0');
    const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}${offsetSign}${offsetHours}:${offsetMinutes}[${timezone}]`;
}

console.log(formatCurrentDate());

function openTaskDialog() {
    const uuid = URL.createObjectURL(new Blob()).slice(-36);
    const timestamps = formatCurrentDate();
    const humanId = prompt('Enter human ID:');
    const humanDescription = prompt('Enter human description:');

    const taskData = {
        uuid: uuid,
        clientTimeStamp: timestamps,
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
