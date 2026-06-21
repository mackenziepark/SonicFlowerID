const recordBtn = document.getElementById("recordBtn");
const nextBtn = document.getElementById("nextBtn");
const message = document.getElementById("recordMessage");

let mediaRecorder;
let audioChunks = [];
let isRecording = false;
let currentStream = null;

recordBtn.addEventListener("click", async () => {
    if (!isRecording) {
        await startRecording();
    } else {
        stopRecording();
    }
});

async function startRecording() {
    try {
        currentStream = await navigator.mediaDevices.getUserMedia({ audio: true });

        mediaRecorder = new MediaRecorder(currentStream);
        audioChunks = [];

        mediaRecorder.ondataavailable = (event) => {
            audioChunks.push(event.data);
        };

        mediaRecorder.onstop = () => {
            localStorage.setItem("voiceRecorded", "true");
            currentStream.getTracks().forEach(track => track.stop());
            message.textContent = "✓ Record all done";
        };

        mediaRecorder.start();
        isRecording = true;
        message.textContent = "● Recording... click flower again to stop";

    } catch (error) {
        console.error(error);
        message.textContent = "Microphone permission denied";
    }
}

function stopRecording() {
    if (mediaRecorder && isRecording) {
        mediaRecorder.stop();
        isRecording = false;
    }
}

nextBtn.addEventListener("click", () => {
    window.location.href = "generate.html";

});