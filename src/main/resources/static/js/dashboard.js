window.onload = function () {
  'use strict';

  feather.replace({ 'aria-hidden': 'true' });

  // Fetch data from APIs
  Promise.all([ 
    fetch('http://localhost:8080/api/vehicles').then(response => {
      if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
      return response.json();
    }),
    fetch('http://localhost:8080/api/vehicles/vehicle-date-relationships').then(response => {
      if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
      return response.json();
    })
  ])
  .then(([vehicleDetails, vehicleDateRelationships]) => {
    console.log('Vehicle Details:', vehicleDetails);
    console.log('Vehicle-Date Relationships:', vehicleDateRelationships);

    // Map the date relationships to a dictionary for quick access
    const dateRelationshipMap = new Map();
    vehicleDateRelationships.forEach(relationship => {
      dateRelationshipMap.set(relationship.licensePlate, relationship.datePassed);
    });

    // Merge vehicle details with date relationships and filter out items without a valid datePassed
    const mergedData = vehicleDetails
      .map(vehicle => {
        const datePassed = dateRelationshipMap.get(vehicle.licensePlate);
        if (datePassed) {
          return {
            ...vehicle,
            datePassed: datePassed
          };
        }
        return null; // Filter out vehicles without a valid datePassed
      })
      .filter(item => item !== null); // Remove null items

    console.log('Filtered Merged Data:', mergedData);

    // Insert merged data into the table
    insertTrafficData(mergedData);

    // Generate charts with data
    generateFuelCharts(mergedData); // Use mergedData for accurate chart generation
    generateVehicleCountsChart(mergedData);
    generateEmissionsChart(mergedData);
    generateVehicleTypeChart(mergedData);
  })
  .catch(error => {
    console.error('Error fetching traffic data:', error);
  });

  // Event listeners for sidebar links
  document.getElementById('dashboard-link').addEventListener('click', function () {
    switchTab('dashboard');
  });

  document.getElementById('orders-link').addEventListener('click', function () {
    switchTab('orders');
  });

  document.getElementById('lez-recommendation-link').addEventListener('click', function () {
    switchTab('lez-recommendation');
  });
  document.getElementById('submitRecommendation').addEventListener('click', function() {
    // Get the selected values
    const selectedDate = document.getElementById('lezDate').value;
    const userEmail = document.getElementById('userEmail').value;
    const addEvent = document.getElementById('addEventCheckbox').checked;

    // Verify if date and email were selected
    if (!selectedDate || !userEmail) {
        alert("Please select a date and provide an email.");
        return;
    }

    // Send data directly to Node-RED endpoint
    fetch('http://localhost:1880/getPredictionsAndSendEmail', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            date: selectedDate,
            email: userEmail,
            event: addEvent
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.status === "success") {
            // Show prediction results
            const predictionContainer = document.getElementById('predictionContainer');
            const predictionResult = document.getElementById('predictionResult');

            predictionResult.textContent = `Predicted Emission: ${data.predictions.currentEmissions}\n ZBE Emission: ${data.predictions.zbeEmissions}`;
            predictionContainer.style.display = 'block'; // Make the container visible
            alert('Email sent successfully!');
        } else {
            alert(`There was an issue processing your request: ${data.message}`);
        }
    })
    .catch(error => {
        console.error('Error processing request:', error);
        alert("An error occurred while processing your request. Please try again later.");
    });
});

  
};

// Function to insert data into the table
function insertTrafficData(data) {
  const tableBody = document.getElementById('trafficDataTable')?.getElementsByTagName('tbody')[0];
  if (!tableBody) {
    console.error('Table body not found. Ensure the table exists with the correct ID.');
    return;
  }

  // Clear the table before adding new data
  tableBody.innerHTML = '';

  if (data.length === 0) {
    console.warn('No traffic data to display.');
    return;
  }

  // Populate the table
  data.forEach(item => {
    const row = tableBody.insertRow();

    // Convert emissions to a number and check if it's valid
    const emissions = parseFloat(item.emissionsGPerKM);
    console.log(`Vehicle: ${item.licensePlate}, Emissions: ${emissions}`);

    // Highlight or print for emissions > 140g
    if (!isNaN(emissions) && emissions > 140) {
      row.classList.add('high-emissions'); // Add a class
      console.warn(`High emissions detected: ${item.licensePlate} with ${emissions}g/km`);
    }

    // Define cell data with proper type handling
    const cellData = [
      item.licensePlate,
      item.vehicleType,
      item.brand,
      item.model,
      item.year,
      item.fuel,
      item.engineType,
      item.emissionsGPerKM,
      item.color,
      item.datePassed
    ];

    // Create cells with proper null/undefined handling
    cellData.forEach(data => {
      const cell = row.insertCell();
      cell.textContent = data || 'N/A';
    });
  });
}

// Function to generate fuel comparison charts for two dates
let fuelChart; // Global variable for the fuel comparison chart
let vehicleCountsChart; // Global variable for the vehicle counts chart
let emissionsChart;
let vehicleTypeChart;

// Helper function to generate random colors
function getRandomColor(alpha = 1) {
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);
  return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}

// Function to generate the Fuel Comparison Chart
function generateFuelCharts(data) {
  if (fuelChart) {
    fuelChart.destroy(); // Destroy existing chart instance
  }

  // Aggregate the vehicle count by fuel type and date
  const fuelDataByDate = {};

  data.forEach(vehicle => {
    const fuel = vehicle.fuel || 'Unknown'; // Fuel type
    const date = vehicle.datePassed; // Date of passing

    if (!fuelDataByDate[date]) {
      fuelDataByDate[date] = {}; // Initialize for new date
    }

    if (!fuelDataByDate[date][fuel]) {
      fuelDataByDate[date][fuel] = 0; // Initialize for new fuel type
    }

    fuelDataByDate[date][fuel] += 1; // Increment count for this fuel type
  });

  // Prepare data for the chart
  const dates = Object.keys(fuelDataByDate); // Get all dates
  const fuelTypes = new Set();

  // Collect all unique fuel types
  dates.forEach(date => {
    Object.keys(fuelDataByDate[date]).forEach(fuel => {
      fuelTypes.add(fuel);
    });
  });

  const fuelLabels = Array.from(fuelTypes);
  const datasets = dates.map(date => {
    const counts = fuelLabels.map(fuel => fuelDataByDate[date][fuel] || 0);
    return {
      label: date,
      data: counts,
      backgroundColor: getRandomColor(0.5),
      borderColor: getRandomColor(1),
      borderWidth: 1
    };
  });

  const ctx = document.getElementById('fuelComparisonChart').getContext('2d');

  fuelChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: fuelLabels,
      datasets: datasets
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top'
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

// Function to generate the Vehicle Counts Chart
function generateVehicleCountsChart(data) {
  if (vehicleCountsChart) {
    vehicleCountsChart.destroy(); // Destroy existing chart instance
  }

  // Aggregate the total vehicle counts by date
  const vehicleCountsByDate = {};

  data.sort((a, b) => new Date(a.datePassed) - new Date(b.datePassed));

  data.forEach(vehicle => {
    const date = vehicle.datePassed;

    if (!vehicleCountsByDate[date]) {
      vehicleCountsByDate[date] = 0; // Initialize count
    }

    vehicleCountsByDate[date] += 1; // Increment count
  });

  console.log('Vehicle counts by date:', vehicleCountsByDate);

  // Prepare data for the chart
  const labels = Object.keys(vehicleCountsByDate); // Dates as labels
  const counts = Object.values(vehicleCountsByDate); // Vehicle counts as data

  const ctx = document.getElementById('vehicleCountsChart').getContext('2d');

  vehicleCountsChart = new Chart(ctx, {
    type: 'line', // Line chart for vehicle counts
    data: {
      labels: labels,
      datasets: [
        {
          label: 'Total Vehicles',
          data: counts,
          backgroundColor: getRandomColor(0.3),
          borderColor: getRandomColor(1),
          borderWidth: 2,
          fill: true // Makes the area under the line shaded
        }
      ]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top'
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}
function generateEmissionsChart(data) {
  if (emissionsChart) {
    emissionsChart.destroy(); // Destroy existing chart instance
  }

  // Aggregate total emissions by date
  const emissionsByDate = {};
  
  // Sort data by date first
  data.sort((a, b) => new Date(a.datePassed) - new Date(b.datePassed));

  // Calculate total emissions by date
  data.forEach(vehicle => {
    const date = new Date(vehicle.datePassed).toISOString().split('T')[0];
    const emissions = parseFloat(vehicle.emissionsGPerKM) || 0;
    
    if (!emissionsByDate[date]) {
      emissionsByDate[date] = 0;
    }
    emissionsByDate[date] += emissions;
  });

  console.log('Emissions by date:', emissionsByDate);

  // Convert the data for the chart
  const labels = Object.keys(emissionsByDate).sort();
  const emissionsData = labels.map(date => emissionsByDate[date].toFixed(2)); // Round to 2 decimal places

  const ctx = document.getElementById('emissionsChart').getContext('2d');

  emissionsChart = new Chart(ctx, {
    type: 'bar', // Using bar chart for emissions
    data: {
      labels: labels,
      datasets: [{
        label: 'Total Emissions (g/km)',
        data: emissionsData,
        backgroundColor: 'rgba(255, 99, 132, 0.2)', // Light red
        borderColor: 'rgba(255, 99, 132, 1)', // Red
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top',
        },
        title: {
          display: true,
          text: 'Daily Total Emissions'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return `Total Emissions: ${context.parsed.y.toFixed(2)} g/km`;
            }
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Total Emissions (g/km)'
          }
        },
        x: {
          title: {
            display: true,
            text: 'Date'
          }
        }
      }
    }
  });
}
function generateVehicleTypeChart(data) {
  if (vehicleTypeChart) {
    vehicleTypeChart.destroy();
  }

  // Aggregate vehicle count by type and date
  const typeDataByDate = {};

  // Sort data by date first
  data.sort((a, b) => new Date(a.datePassed) - new Date(b.datePassed));

  // Count vehicles by type for each date
  data.forEach(vehicle => {
    const date = new Date(vehicle.datePassed).toISOString().split('T')[0];
    const type = vehicle.vehicleType || 'Unknown';

    if (!typeDataByDate[date]) {
      typeDataByDate[date] = {};
    }

    if (!typeDataByDate[date][type]) {
      typeDataByDate[date][type] = 0;
    }

    typeDataByDate[date][type]++;
  });

  // Get unique dates and vehicle types
  const dates = Object.keys(typeDataByDate).sort();
  const vehicleTypes = new Set();
  
  dates.forEach(date => {
    Object.keys(typeDataByDate[date]).forEach(type => {
      vehicleTypes.add(type);
    });
  });

  // Convert to array and sort vehicle types
  const typeLabels = Array.from(vehicleTypes).sort();

  // Create datasets for each date
  const datasets = dates.map(date => {
    const counts = typeLabels.map(type => typeDataByDate[date][type] || 0);
    const color = getRandomColor(0.7);
    return {
      label: date,
      data: counts,
      backgroundColor: color,
      borderColor: color,
      borderWidth: 1
    };
  });

  const ctx = document.getElementById('vehicleTypeChart').getContext('2d');

  vehicleTypeChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: typeLabels,
      datasets: datasets
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top',
        },
        title: {
          display: true,
          text: 'Vehicle Types by Date'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return `${context.dataset.label}: ${context.parsed.y} vehicles`;
            }
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Number of Vehicles'
          },
          ticks: {
            stepSize: 1
          }
        },
        x: {
          title: {
            display: true,
            text: 'Vehicle Type'
          }
        }
      }
    }
  });
}

// Function to switch tabs
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

