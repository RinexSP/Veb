function drawChart(point = null) {
    const canvas = document.getElementById('grafik');
    const context = canvas.getContext('2d');
    const width = canvas.width;
    const height = canvas.height;
    const pixelRatio = width / 3;


    context.clearRect(0, 0, width, height);


    context.translate(width / 2, height / 2);


    context.scale(1, -1);

    context.lineWidth = 3;
    context.strokeStyle = 'black';


    context.beginPath();
    context.moveTo(-width / 2, 0);
    context.lineTo(width / 2, 0);
    context.moveTo(0, -height / 2);
    context.lineTo(0, height / 2);
    context.stroke();


    context.fillStyle = "blue";
    context.globalAlpha = 0.5;


    context.beginPath();
    context.arc(0, 0, pixelRatio, 0, Math.PI / 2, false);
    context.lineTo(0, 0);
    context.fill();


    context.beginPath();
    context.moveTo(0, 0);
    context.lineTo(-pixelRatio, 0);
    context.lineTo(0, -pixelRatio);
    context.closePath();
    context.fill();


    context.fillRect(0, -pixelRatio / 2, pixelRatio, pixelRatio / 2);


    context.scale(1, -1);
    context.fillStyle = "black";
    context.font = "14px Arial";


    context.fillText("x", width / 2 - 20, -10);
    context.fillText("y", 10, -height / 2 + 20);


    context.fillText("R", pixelRatio, 20);
    context.fillText("R/2", pixelRatio / 2, 20);
    context.fillText("-R/2", -pixelRatio / 2, 20);
    context.fillText("-R", -pixelRatio, 20);


    context.fillText("R", -25, -pixelRatio);
    context.fillText("R/2", -25, -pixelRatio / 2);
    context.fillText("-R/2", -30, pixelRatio / 2);
    context.fillText("-R", -25, pixelRatio);


    context.scale(1, -1);
    context.beginPath();


    for (let i of [-pixelRatio, -pixelRatio / 2, pixelRatio / 2, pixelRatio]) {
        context.moveTo(i, -5);
        context.lineTo(i, 5);
    }


    for (let i of [-pixelRatio, -pixelRatio / 2, pixelRatio / 2, pixelRatio]) {
        context.moveTo(-5, i);
        context.lineTo(5, i);
    }

    context.stroke();


    if (point) {
        context.fillStyle = 'red';
        context.beginPath();
        context.arc(point.x * pixelRatio, point.y * pixelRatio, 5, 0, 2 * Math.PI);
        context.fill();
    }


    context.setTransform(1, 0, 0, 1, 0, 0);
}

window.onload = function () {
    drawChart();
};

window.gerEventListener('load', function () {
    drawChart();
});



document.getElementById('dataForm').addEventListener('submit', handleFormSubmit);

function handleFormSubmit(event) {
    event.preventDefault();
    const loader = document.getElementById("loader");
    loader.classList.remove('hidden');

    const xCoord = parseFloat(document.getElementById('x-coord-input').value);
    const yCoord = parseFloat(document.getElementById('y-coord-input').value);
    const rValue = [...document.getElementsByName('r')].find(radio => radio.checked)?.value;

    if (isNaN(xCoord) || isNaN(yCoord) || !rValue) {
        loader.classList.add('hidden');
        return;
    }

     const xMin = -3;
     const xMax = 3;
     const yMin = -5;
     const yMax = 3;


      if (isNaN(xCoord) || isNaN(yCoord) || !rValue || xCoord < xMin || xCoord > xMax || yCoord < yMin || yCoord > yMax) {
          loader.classList.add('hidden');
           alert("Значение не соответствует диапазону. Пожалуйста, введите правильные значения для x или y.");
           return;
        }

    const coords = { x: xCoord, y: yCoord, r: parseFloat(rValue) };

    console.log(coords);

    const startTime = performance.now();

    fetch('/api/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(coords)
    })
    .then(response => {
    console.log("Response from server:", response);
        if (!response.ok) {
            throw new Error('Сетевая ошибка: ' + response.status);
        }
        return response.json().catch(err => {
                throw new Error('Ошибка парсинга JSON: ' + err.message);
            });
    })

    .then(data => {

        console.log("Response from server:", data);
        console.log("Coordinates received: x =", data.x, ", y =", data.y, ", r =", data.r, ", result =", data.result);
        const endTime = performance.now();
        const elapsedTime = endTime - startTime;

        const currentTime = new Date().toISOString().slice(0, 19).replace('T', ' ');
        const tableBody = document.getElementById("results-table").getElementsByTagName("tbody")[0];
        data.y = parseFloat(data.y);
        const row = tableBody.insertRow();
        row.insertCell(0).innerText = data.x;
        row.insertCell(1).innerText = data.y;
        row.insertCell(2).innerText = data.r;
        row.insertCell(3).innerText = data.result ? "OK" : "MISS";
        row.insertCell(4).innerText = currentTime;
        row.insertCell(5).innerText = elapsedTime.toFixed(2) + " ms";
        loader.classList.add('hidden');


        drawChart({ x: coords.x / coords.r, y: coords.y / coords.r });
    })
    .catch(error => {
        console.error('Ошибка:', error);
        alert("Ошибка на сервере!");
        loader.classList.add('hidden');
    });
}


function toggleLoader() {
    const loader = document.getElementById('loader');
    loader.classList.toggle('hidden');
}