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

    row.insertCell().textContent = item.licensePlate || 'N/A'; // License Plate
    row.insertCell().textContent = item.vehicleType || 'N/A'; // Vehicle Type
    row.insertCell().textContent = item.brand || 'N/A'; // Brand
    row.insertCell().textContent = item.model || 'N/A'; // Model
    row.insertCell().textContent = item.year || 'N/A'; // Year
    row.insertCell().textContent = item.fuel || 'N/A'; // Fuel
    row.insertCell().textContent = item.engineType || 'N/A'; // Engine Type
    row.insertCell().textContent = item.emissionsGPerKM || 'N/A'; // Emissions (g/km)
    row.insertCell().textContent = item.color || 'N/A'; // Color
    row.insertCell().textContent = item.datePassed || 'N/A'; // Date Passed
  });
}

// Function to generate fuel comparison charts for two dates
function generateFuelCharts(data) {
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

  console.log(fuelDataByDate); // Check the aggregated data

  // Prepare data for chart
  const dates = Object.keys(fuelDataByDate); // Get all dates (keys)
  const fuelTypes = new Set(); // Set to store unique fuel types

  // Collect all unique fuel types
  dates.forEach(date => {
    Object.keys(fuelDataByDate[date]).forEach(fuel => {
      fuelTypes.add(fuel);
    });
  });

  const fuelLabels = Array.from(fuelTypes); // Convert set to array for chart
  const datasets = dates.map(date => {
    const counts = fuelLabels.map(fuel => fuelDataByDate[date][fuel] || 0); // Fill with 0s if no vehicles for a fuel type
    return {
      label: date,
      data: counts,
      backgroundColor: getRandomColor(),
      borderColor: getRandomColor(),
      borderWidth: 1
    };
  });

  const ctx = document.getElementById('fuelComparisonChart').getContext('2d');

  // Create the chart
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: fuelLabels, // Fuel types as X-axis labels
      datasets: datasets // Each date will have its own dataset
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

// Helper function to generate random colors for chart
function getRandomColor() {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
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
