// new chart
new Chart(document.getElementById("chart"), {
    type: 'bar',
    data: {
      labels: [["Bitcoin","Final de 2018"],["Market","Final de 2018"],["Línea","Final de 2018"],["Audace","Final de 2018"]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [30,45,75,125]
        },
      ]
    },
    options: {
      legend: {
                  display: false,
                  labels: {
                      fontColor: "#C0C0C0",
                      fontSize: 18
                  }
              },
              scales: {
                  yAxes: [{
                      ticks: {
                          fontColor: "#C0C0C0",
                          beginAtZero: true
                      },
                      gridLines: {
                        color: "#C0C0C0",
                        lineWidth: 0.5
                      }
                  }],
                  xAxes: [{
                      ticks: {
                          fontColor: "#C0C0C0",
                          beginAtZero: true
                      },
                       gridLines: {
                         color: "#C0C0C0",
                         lineWidth: 0.5
                       }
                  }]
              }
          }
});

// new chart
new Chart(document.getElementById("chart2"), {
    type: 'bar',
    data: {
      labels: [["Bitcoin","Final de 2018"],["Market","Final de 2018"],["Línea","Final de 2018"],["Audace","Final de 2018"]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [3.5,4.5,7,11]
        },
      ]
    },
    options: {
      legend: {
                  display: false,
                  labels: {
                      fontColor: "#C0C0C0",
                      fontSize: 18
                  }
              },
              scales: {
                  yAxes: [{
                      ticks: {
                          fontColor: "#C0C0C0",
                          beginAtZero: true
                      },
                      gridLines: {
                        color: "#C0C0C0",
                        lineWidth: 0.5
                      }
                  }],
                  xAxes: [{
                      ticks: {
                          fontColor: "#C0C0C0",
                          beginAtZero: true
                      },
                       gridLines: {
                         color: "#C0C0C0",
                         lineWidth: 0.5
                       }
                  }]
              }
          }
});