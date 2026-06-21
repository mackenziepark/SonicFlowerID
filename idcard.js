const nameText = document.getElementById("cardName");
const birthdayText = document.getElementById("cardBirthday");
const dateText = document.getElementById("cardDate");
const flowerImage = document.getElementById("flowerImage");

const latestFlowerId =
    localStorage.getItem("latestFlowerId");

const flowerRecord =
    latestFlowerId
        ? JSON.parse(localStorage.getItem(`flower_${latestFlowerId}`))
        : null;

if (flowerRecord) {
    nameText.textContent = flowerRecord.name;
    birthdayText.textContent = formatBirthday(flowerRecord.birthday);
    flowerImage.src = flowerRecord.image;
} else {
    nameText.textContent = "";
    birthdayText.textContent = "";
}

dateText.textContent = getTodayDate();

function formatBirthday(value) {
    const clean = value.replace(/[^0-9]/g, "");

    if (clean.length !== 8) {
        return value;
    }

    const yyyy = clean.substring(0, 4);
    const mm = clean.substring(4, 6);
    const dd = clean.substring(6, 8);

    return `${dd}. ${mm}. ${yyyy}`;
}

function getTodayDate() {
    const today = new Date();

    const dd = String(today.getDate()).padStart(2, "0");
    const mm = String(today.getMonth() + 1).padStart(2, "0");
    const yyyy = today.getFullYear();

    return `${dd}. ${mm}. ${yyyy}`;
}