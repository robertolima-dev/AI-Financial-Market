// linea chart
new Chart(document.getElementById("chart"), {
    type: 'bar',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [71.37,85.73,-0.07,11.9,46.95,-12.84,41.03,21.65,75.28,4.17,8.47,2.76]
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

// audace chart
new Chart(document.getElementById("chart2"), {
    type: 'bar',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [139.42,88.34,3.86,14.52,84.78,-19.74,4.81,56.8,48.23,6.77,10.76,4.40]
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

//market graph
new Chart(document.getElementById("chart3"), {
    type: 'bar',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [47.29,121.05,16.67,-6.84,90.58,-15.52,25.85,64.86,96.72,-18.55,-10.36,-46.9]
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

//bitoin
new Chart(document.getElementById("chart4"), {
    type: 'bar',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [27.56,72.99,4.64,15.52,67.54,-10.83,49.53,52.50,39.96,-28.57,4.10,-36.00]
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

//linea x audace
new Chart(document.getElementById("chart5"), {
    type: 'line',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#f4b942",
          fill: false,
          borderColor: "#f4b942",
          data: [71.37,85.73,-0.07,11.9,46.95,-12.84,41.03,21.65,75.28,4.17,8.47,2.76],
          label: "Línea"
        },
         {
           backgroundColor: "#53f441",
           fill: false,
           borderColor: "#53f441",
           data: [139.42,88.34,3.86,14.52,84.78,-19.74,4.81,56.8,48.23,6.77,10.76,4.40],
           label: "Audace"
         }
      ]
    },
    options: {
      legend: {
                  display: true,
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

//Bitcoin vs audace
new Chart(document.getElementById("chart6"), {
    type: 'line',
    data: {
      labels: [["abril",""],["maio",""],["junho",""],["julho",""],["agosto",""],["setembro",""],["outubro",""],["novembro",""],["dezembro",""],["janeiro","2018"],["fevereiro",""],["março",""]],
      datasets: [
        {
          backgroundColor: "#f4b942",
          borderColor: "#f4b942",
          fill: false,
          data: [27.56,72.99,4.64,15.52,67.54,-10.83,49.53,52.50,39.96,-28.57,4.10,-36.00],
          label: "Bitcoin"
        },
         {
           backgroundColor: "#53f441",
           borderColor: "#53f441",
           fill: false,
           data: [139.42,88.34,3.86,14.52,84.78,-19.74,4.81,56.8,48.23,6.77,10.76,4.40],
           label: "Audace"
         }
      ]
    },
    options: {
      legend: {
                  display: true,
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
                          fontColor: "white",
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

//head to head
new Chart(document.getElementById("chart7"), {
    type: 'bar',
    data: {
      labels: [["Comparação","Ultimo","Ano"]],
      datasets: [
        {
          backgroundColor: "#fff",
          data: [505],
          label: "Bitcoin"
        },
        {
          backgroundColor: "#fff",
          data: [776],
          label: "Market"
        },
        {
          backgroundColor: "#fff",
          data: [1380],
          label: "Linea"
        },
        {
          backgroundColor: "#fff",
          data: [2295],
          label: "Audace"
        }
      ]
    },
    options: {
      legend: {
                  display: true,
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