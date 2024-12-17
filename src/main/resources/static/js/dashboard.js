/* globals Chart:false, feather:false */

window.onload = function() {
  'use strict';

  feather.replace({ 'aria-hidden': 'true' });

  // Fetch traffic data from the API
  fetch('http://localhost:8080/api/traffic-data')
      .then(response => {
          if (!response.ok) {
              throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json(); // Parse the JSON response
      })
      .then(data => {
          console.log(data); // Check the data structure in the console
          insertTrafficData(data);
          generateCharts(data); // Call the function to generate all charts
      })
      .catch(error => {
          console.error('Error fetching traffic data:', error);
      });

  // Event listeners for sidebar links
  document.getElementById('dashboard-link').addEventListener('click', function() {
      switchTab('dashboard');
  });

  document.getElementById('orders-link').addEventListener('click', function() {
      switchTab('orders');
  });

  document.getElementById('lez-recommendation-link').addEventListener('click', function() {
      switchTab('lez-recommendation');
  });
};

// Switch tabs based on which link is clicked
function switchTab(tabName) {
  // Hide all sections
  const sections = document.querySelectorAll('.charts-section, .table-section, .lez-recommendation-section');
  sections.forEach(section => section.classList.remove('active'));

  // Highlight the active link
  document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));

  if (tabName === 'dashboard') {
    document.querySelector('.charts-section').classList.add('active');
    document.getElementById('dashboard-link').classList.add('active');
  } else if (tabName === 'orders') {
    document.querySelector('.table-section').classList.add('active');
    document.getElementById('orders-link').classList.add('active');
  } else if (tabName === 'lez-recommendation') {
    document.querySelector('.lez-recommendation-section').classList.add('active');
    document.getElementById('lez-recommendation-link').classList.add('active');
  }
}

// Function to generate charts
function generateCharts(data) {
  const ctx1 = document.getElementById('vehicleCountsChart').getContext('2d');
  const ctx2 = document.getElementById('avgSpeedChart').getContext('2d');
  const ctx3 = document.getElementById('trafficDensityChart').getContext('2d');
  const ctx4 = document.getElementById('emissionsChart').getContext('2d'); // New chart for emissions

  const labels = data.map(item => item.trafficId);
  const vehicleCounts = data.map(item => item.carCount);
  const avgSpeed = data.map(item => item.avgSpeed);
  const trafficDensity = data.map(item => item.trafficDensity);
  const totalEmissions = data.map(item => item.totalEmissions); // New data for total emissions

  // Vehicle Counts Chart
  new Chart(ctx1, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Car Count',
        data: vehicleCounts,
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });

  // Average Speed Chart
  new Chart(ctx2, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Avg Speed',
        data: avgSpeed,
        fill: false,
        borderColor: 'rgba(75, 192, 192, 1)',
        tension: 0.1
      }]
    }
  });

  // Traffic Density Chart
  new Chart(ctx3, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Traffic Density',
        data: trafficDensity,
        fill: false,
        borderColor: 'rgba(153, 102, 255, 1)',
        tension: 0.1
      }]
    }
  });

  // Emissions Chart (new)
  new Chart(ctx4, {
    type: 'bar', // Or line, depending on your preference
    data: {
      labels: labels,
      datasets: [{
        label: 'Total Emissions',
        data: totalEmissions,
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

// Insert traffic data into the table
/*
function insertTrafficData(data) {
  const tableBody = document.getElementById('trafficDataTable').getElementsByTagName('tbody')[0];
  data.forEach(item => {
    const row = tableBody.insertRow();
    row.insertCell().textContent = item.trafficId;
    row.insertCell().textContent = item.timestamp;
    row.insertCell().textContent = item.carCount;
    row.insertCell().textContent = item.busCount;
    row.insertCell().textContent = item.truckCount;
    row.insertCell().textContent = item.motorcycleCount;
    row.insertCell().textContent = item.avgSpeed;
    row.insertCell().textContent = item.trafficDensity;
    row.insertCell().textContent = item.congestionLevel;
    row.insertCell().textContent = item.totalEmissions; // New data for emissions
  });
}*/
// Clear existing table data before inserting new data
// Insert traffic data into the table
function insertTrafficData(data) {
  const tableBody = document.getElementById('trafficDataTable').getElementsByTagName('tbody')[0];
  data.forEach(item => {
    const row = tableBody.insertRow();
    row.insertCell().textContent = item.License_Plate; // License Plate
    row.insertCell().textContent = item.Vehicle_Type; // Vehicle Type
    row.insertCell().textContent = item.Brand; // Brand
    row.insertCell().textContent = item.Model; // Model
    row.insertCell().textContent = item.Year; // Year
    row.insertCell().textContent = item.Fuel; // Fuel
    row.insertCell().textContent = item.Engine_Type; // Engine Type
    row.insertCell().textContent = item.Emissions_G_per_KM; // Emissions in g/km
    row.insertCell().textContent = item.Color; // Color
    row.insertCell().textContent = item.Date_Passed; // Date Passed
  });
};
