
var canvas = document.getElementById('myChart');
var ctx = canvas.getContext('2d');

Chart.defaults.global.legend.display = true;
Chart.defaults.global.legend.position = 'right';
Chart.defaults.global.legend.labels.fontColor = 'white';
Chart.defaults.global.maintainAspectRatio = false;
Chart.defaults.global.tooltips.callbacks.afterLabel = function () {
    return "hours";
};


// Datas

//grands
var general = {
    labels: [ 'Jobs', 'Study','Work', 'Relaxation', 'Health', 'Sleep'],
    datasets: [{
        label: "General",
        backgroundColor: ['#AEC7E8',  '#98DF8A', '#FFBB78', '#FF9896', '#C5B0D5', '#C49C94', '#F7B6D2', '#C7C7C7', '#DBDB8D', '#9EDAE5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [403,311,123, 109, 145, 654],
    }],
};

//parents
var relaxation = {
    labels: ['Social', 'Entertainment'],
    datasets: [{
        label: "Relaxation",
        backgroundColor: ['#EF8886','#FF9896'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [55, 54],
    }]
}
var study = {
    labels: ['Hobby', 'Learning', 'School'],
    datasets: [{
        label: "Study",
        backgroundColor: ['#78BF6A','#88CF7A','#98DF8A'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [53, 238, 20],
    }]
}
var work = {
    labels: ['Productivity', 'Projects'],
    datasets: [{
        label: "Work",
        backgroundColor: ['#EFAB68','#FFBB78'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [19, 104],
    }]
}
var health = {
    labels: ['Exercise', 'Life', 'Mindfulness'],
    datasets: [{
        label: "Health",
        backgroundColor: ['#A590B5','#B5A0C5','#C5B0D5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [51, 94, 32],
    }]
}
var jobs = {
    labels: ['Waiter', 'Volunteer'],
    datasets: [{
        label: "Jobs",
        backgroundColor: ['#AEC7E8', '#73C2FB', '#FFBB78', '#98DF8A', '#FF9896', '#C5B0D5', '#C49C94', '#F7B6D2', '#C7C7C7', '#DBDB8D', '#9EDAE5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [373, 30],
    }]
}

//Chidren
var entertainment = {
    labels: ['YouTube', 'TV Shows'],
    datasets: [{
        label: "Entertainment",
        backgroundColor: ['#EF8886','#FF9896'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [51, 3],
    }]
}
var exercise = {
    labels: ['Biking', 'Running', 'Working Out', 'Basketball'],
    datasets: [{
        label: "Exercise",
        backgroundColor: ['#9580A5','#A590B5','#B5A0C5','#C5B0D5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [14, 12, 15, 10],
    }]
}
var hobby = {
    labels: ['Guitar'],
    datasets: [{
        label: "Hobby",
        backgroundColor: ['#78BF6A'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [53],
    }]
}
var learning = {
    labels: ['Web Development', 'Reading', 'Elocution', 'College Search', 'Memorize Vocab'],
    datasets: [{
        label: "Learning",
        backgroundColor: ['#589F4A','#68AF5A','#78BF6A','#88CF7A','#98DF8A'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [157, 6, 5, 65, 5],
    }]
}
var life = {
    labels: ['Wash Up', 'Eat', 'Restroom', 'Shower', 'Hospital'],
    datasets: [{
        label: "Life",
        backgroundColor: ['#857095','#9580A5','#A590B5','#B5A0C5','#C5B0D5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [18, 57, 4, 9, 6],
    }]
}
var mindfulness = {
    labels: ['Journal', 'Meditation'],
    datasets: [{
        label: "Mindfulness",
        backgroundColor: ['#A590B5','#B5A0C5','#C5B0D5'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [29, 3],
    }]
}
var productivity = {
    labels: ['Research', 'Computer', 'Housework', 'Errands'],
    datasets: [{
        label: "Productivity",
        backgroundColor: ['#CF8B48','#DF9B58','#EFAB68','#FFBB78'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [2, 10, 4, 3],
    }]
}
var projects = {
    labels: ['Website (this one)', 'Possessions'],
    datasets: [{
        label: "Projects",
        backgroundColor: ['#EFAB68','#FFBB78'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [86, 18],
    }]
}
var school = {
    labels: ['Driver\'s Ed'],
    datasets: [{
        label: "School",
        backgroundColor: ['#98DF8A'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [20],
    }]
}
var social = {
    labels: ['Parents', 'Friends', 'Others'],
    datasets: [{
        label: "Social",
        backgroundColor: ['#CF6866','#DF7876','#EF8886'],
        borderColor: '#313131',
        borderWidth: 2,
        hoverBorderWidth: 4,
        data: [12, 37, 6],
    }]
}

function load(){
    var chart = new Chart(ctx, {
        type: 'pie',
        data: general,
        options: {
            title: {
                display: true,
                text:'My Summer',
                fontColor: 'white'
            }
        }
    });
    
    var levelCounter = 0;
    var previousLevel = null;
    
    canvas.onclick = function (evt) {
        var activePoints = chart.getElementsAtEvent(evt);
        if (activePoints[0]) {
            var name = chart.data.labels[activePoints[0]['_index']].toLowerCase();
            if (window[name]) {
                createChart(window[name]);
            }
        }
    }
    
    function createChart(newData) {
        levelCounter++;
        previousLevel = chart.data.datasets[0].label;
        chart.destroy();
        chart = new Chart(ctx, {
            type: 'doughnut',
            data: newData,
            options: {
                title: {
                    display: true,
                    text: newData.datasets[0].label,
                    position: 'top',
                    fontColor: 'white',
                }
            }
        })
        WidthChange(mq);
        arrow.classList.remove("hidden");
    }
    
    
} 