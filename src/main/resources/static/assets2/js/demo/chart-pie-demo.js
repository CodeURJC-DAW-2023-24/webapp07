// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function cargarlatarta(lab, dat) {
  if(lab.length == 0){
    lab = ["No donations"]
    dat = [100]
  }
  // Pie Chart Example
  ne = lab.length/3 + 1
  bcb = ['#4e73df', '#1cc88a', '#36b9cc']
  hbcb =  ['#2e59d9', '#17a673', '#2c9faf']
  bc = []
  hbc = []
  for (i = 0; i < ne; i++) {
    bc = bc.concat(bcb)
    hbc = hbc.concat(hbcb)
  }
  var ctx = document.getElementById("myPieChart");
  var myPieChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: lab,//["Direct", "Referral", "Social"],
      datasets: [{
        data: dat,// [55, 30, 15],
        backgroundColor: bc,//['#4e73df', '#1cc88a', '#36b9cc'],
        hoverBackgroundColor: hbc,//['#2e59d9', '#17a673', '#2c9faf'],
        hoverBorderColor: "rgba(234, 236, 244, 1)",
      }],
    },
    options: {
      maintainAspectRatio: false,
      tooltips: {
        backgroundColor: "rgb(255,255,255)",
        bodyFontColor: "#858796",
        borderColor: '#dddfeb',
        borderWidth: 1,
        xPadding: 15,
        yPadding: 15,
        displayColors: false,
        caretPadding: 10,
      },
      legend: {
        display: false
      },
      cutoutPercentage: 80,
    },
  });
}