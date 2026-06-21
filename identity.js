const nextBtn = document.getElementById("nextBtn");
const nameInput = document.getElementById("nameInput");
const birthdayInput = document.getElementById("birthdayInput");

nextBtn.addEventListener("click", () => {
    localStorage.setItem("userName", nameInput.value.trim());
    localStorage.setItem("birthday", birthdayInput.value.trim());

    window.location.href = "voice.html";
});