const tipsData = [
    {
        id: 1,
        category: "Paddy",
        instruction: "Apply Neem cake during land preparation. It prevents stem borer attacks.",
        kannadaInstruction: "ಭೂಮಿ ಸಿದ್ಧಪಡಿಸುವಾಗ ಬೇವಿನ ಹಿಂಡಿ ಹಾಕಿ. ಇದು ಕಾಂಡ ಕೊರೆಯುವ ಹುಳುವನ್ನು ತಡೆಯುತ್ತದೆ.",
        imageUrl: "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Paddy_field_in_Bangladesh.jpg/800px-Paddy_field_in_Bangladesh.jpg",
        isSuccessStory: false
    },
    {
        id: 2,
        category: "Areca nut",
        instruction: "Spray 1% Bordeaux mixture before monsoon. It prevents fruit rot disease.",
        kannadaInstruction: "ಮಳೆಗಾಲಕ್ಕೆ ಮುಂಚೆ 1% ಬೋರ್ಡೋ ಮಿಶ್ರಣ ಸಿಂಪಡಿಸಿ. ಇದು ಕೊಳೆ ರೋಗ ತಡೆಯುತ್ತದೆ.",
        imageUrl: "https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Arecanut_on_Tree.JPG/800px-Arecanut_on_Tree.JPG",
        isSuccessStory: true,
        farmerName: "Siddappa from Shimoga"
    },
    {
        id: 3,
        category: "Tomato",
        instruction: "Use sticky yellow traps in your field. It catches whiteflies effectively.",
        kannadaInstruction: "ನಿಮ್ಮ ಹೊಲದಲ್ಲಿ ಹಳದಿ ಬಣ್ಣದ ಅಂಟು ಬಲೆ ಬಳಸಿ. ಇದು ಬಿಳಿನೊಣಗಳನ್ನು ಪರಿಣಾಮಕಾರಿಯಾಗಿ ಹಿಡಿಯುತ್ತದೆ.",
        imageUrl: "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/800px-Tomato_je.jpg",
        isSuccessStory: false
    },
    {
        id: 4,
        category: "Coconut",
        instruction: "Provide adequate summer irrigation and mulch the basin. Prevents button dropping.",
        kannadaInstruction: "ಬೇಸಿಗೆಯಲ್ಲಿ ಸಾಕಷ್ಟು ನೀರು ಒದಗಿಸಿ ಮತ್ತು ಬುಡಕ್ಕೆ ಹೊದಿಕೆ ಹಾಕಿ. ಇದು ಕಾಯಿ ಉದುರುವುದನ್ನು ತಡೆಯುತ್ತದೆ.",
        imageUrl: "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Coconut_tree_Kerala.jpg/800px-Coconut_tree_Kerala.jpg",
        isSuccessStory: false
    }
];

let swiper;

function initSwiper() {
    swiper = new Swiper(".mySwiper", {
        spaceBetween: 20,
        pagination: {
            el: ".swiper-pagination",
            clickable: true,
        },
    });
}

function renderCards(category) {
    const wrapper = document.getElementById('swiperWrapper');
    wrapper.innerHTML = '';

    const filteredData = category === 'All' 
        ? tipsData 
        : tipsData.filter(tip => tip.category === category);

    filteredData.forEach(tip => {
        const slide = document.createElement('div');
        slide.className = 'swiper-slide';

        const successBadge = tip.isSuccessStory ? `<div class="success-label">Success Story</div>` : '';
        const farmerInfo = tip.isSuccessStory ? `<div class="farmer-name">Farmer: ${tip.farmerName}</div>` : '';

        slide.innerHTML = `
            <div class="card-image-container">
                <img src="${tip.imageUrl}" alt="${tip.category}" class="card-image">
                ${successBadge}
            </div>
            <div class="card-content">
                ${farmerInfo}
                <div class="instruction-eng">${tip.instruction}</div>
                <div class="instruction-kan">${tip.kannadaInstruction}</div>
            </div>
        `;
        wrapper.appendChild(slide);
    });

    if (swiper) {
        swiper.destroy(true, true);
    }
    initSwiper();
}

// Event Listeners
document.querySelectorAll('.chip').forEach(chip => {
    chip.addEventListener('click', (e) => {
        // Remove active class from all
        document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
        // Add to clicked
        e.target.classList.add('active');
        // Filter
        renderCards(e.target.dataset.category);
    });
});

document.getElementById('fabAskExpert').addEventListener('click', () => {
    alert('Expert Ask Simulation: Please capture or upload a photo of the diseased leaf.');
});

// Initial Render
renderCards('All');
