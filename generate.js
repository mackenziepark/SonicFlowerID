const idCardBtn = document.getElementById("idCardBtn");
const generateBtn = document.getElementById("generateBtn");
const canvas = document.getElementById("flowerCanvas");
const ctx = canvas.getContext("2d");

canvas.width = 800;
canvas.height = 800;

generateBtn.addEventListener("click", async () => {

    loadingMessage.textContent =
        "Generating your Sonic Flower...";

    loadingMessage.style.display = "block";

    // 먼저 loading 문구를 화면에 그리게 함
    await new Promise(resolve =>
        requestAnimationFrame(resolve)
    );

    await new Promise(resolve =>
        setTimeout(resolve, 100)
    );

    const name =
        localStorage.getItem("userName") || "";

    const birthday =
        localStorage.getItem("birthday") || "";

    const voiceValue =
        Number(localStorage.getItem("voiceValue")) || 50;

    const peakFrequency =
        Number(localStorage.getItem("peakFrequency")) || 200;

    const flowerData =
        await generateFlowerData(
            name,
            birthday,
            voiceValue,
            peakFrequency
        );

    drawFlower(flowerData);

const flowerImage =

    canvas.toDataURL("image/png");

const sonicId =

    Date.now();

const flowerRecord = {

    id: sonicId,

    name: name,

    birthday: birthday,

    voiceValue: voiceValue,

    peakFrequency: peakFrequency,

    image: flowerImage

};

localStorage.setItem(

    `flower_${sonicId}`,

    JSON.stringify(flowerRecord)

);

localStorage.setItem(

    "latestFlowerId",

    sonicId

);

loadingMessage.style.display = "none";
});

async function generateFlowerData(name, birthday, voiceValue, peakFrequency) {
    const normalizedName = name
        .trim()
        .toLowerCase()
        .replace(/\s+/g, "");

    const normalizedBirthday = birthday
        .trim()
        .replace(/[^0-9]/g, "");

    const text = normalizedName + normalizedBirthday;
    const hashText = await makeSHA256(text);

    const birthMonth = getBirthMonth(normalizedBirthday);
    const palette = getSeasonPalette(birthMonth);

    const colorIndex =
        parseInt(hashText.substring(0, 2), 16) % palette.length;

    const color = palette[colorIndex];

    const flowerType = getFlowerTypeFromMonth(birthMonth);

    const petalCount =
        5 + parseInt(hashText.substring(2, 4), 16) % 6;

    const petalSize =
        180 + voiceValue;

    const rotation =
        parseInt(hashText.substring(4, 8), 16) % 360;

    const peak = peakFrequency;

    const petalLengthFactor =
        0.90 + (peak % 300) / 300.0 * 0.35;

    const petalWidthFactor =
        0.05 + (peak % 180) / 180.0 * 0.25;

    const petalTipFactor =
        0.05 + (peak % 220) / 220.0 * 0.35;

    const waveFactor =
        0.03 + (peak % 300) / 300.0 * 0.12;

    return {
        color,
        flowerType,
        petalCount,
        petalSize,
        rotation,
        petalLengthFactor,
        petalWidthFactor,
        petalTipFactor,
        waveFactor,
        hashText
    };
}

async function makeSHA256(input) {
    const encoder = new TextEncoder();
    const data = encoder.encode(input);

    const hashBuffer = await crypto.subtle.digest("SHA-256", data);

    const hashArray = Array.from(new Uint8Array(hashBuffer));

    return hashArray
        .map(byte => byte.toString(16).padStart(2, "0"))
        .join("");
}

function getBirthMonth(birthday) {
    if (birthday.length >= 6) {
        return Number(birthday.substring(4, 6));
    }

    return 1;
}

function getSeasonPalette(month) {
    if (month === 3 || month === 4 || month === 5) {
        return ["#DE8A8A", "#E7D74B", "#5F9E4B", "#5492DB"];
    } else if (month === 6 || month === 7 || month === 8) {
        return ["#DCA1C3", "#80BC96", "#94C7E6", "#727FBD"];
    } else if (month === 9 || month === 10 || month === 11) {
        return ["#BA271E", "#D27840", "#3D4531", "#3B0E33"];
    } else {
        return ["#CD3F85", "#76154F", "#286064", "#110664"];
    }
}

function getFlowerTypeFromMonth(month) {
    if (month === 3 || month === 4 || month === 5) {
        return "CHERRY_BLOSSOM";
    } else if (month === 6 || month === 7 || month === 8) {
        return "LOTUS";
    } else if (month === 9 || month === 10 || month === 11) {
        return "CHRYSANTHEMUM";
    } else {
        return "CAMELLIA";
    }
}

function drawFlower(flowerData) {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const cx = canvas.width / 2;
    const cy = canvas.height / 2 - 20;

    drawStemHash(flowerData, cx, cy);

    if (flowerData.flowerType === "CHERRY_BLOSSOM") {
        drawHashPetalLayer(flowerData, cx, cy, 5, 1.0, 0.75, 0);
    } else if (flowerData.flowerType === "LOTUS") {
        drawHashPetalLayer(flowerData, cx, cy, 8, 1.25, 0.45, 0);
        drawHashPetalLayer(flowerData, cx, cy, 6, 0.85, 0.35, Math.PI / 8);
    } else if (flowerData.flowerType === "CHRYSANTHEMUM") {
        drawHashPetalLayer(flowerData, cx, cy, flowerData.petalCount * 3, 1.05, 0.22, 0);
    } else if (flowerData.flowerType === "CAMELLIA") {
        drawHashPetalLayer(flowerData, cx, cy, 7, 1.0, 0.75, 0);
        drawHashPetalLayer(flowerData, cx, cy, 6, 0.75, 0.60, Math.PI / 7);
    }

    drawCenter(flowerData, cx, cy);
}

function drawHashPetalLayer(flowerData, cx, cy, petals, lengthRatio, widthRatio, offset) {
    for (let i = 0; i < petals; i++) {
        const angle =
            (2 * Math.PI / petals) * i +
            degreesToRadians(flowerData.rotation) +
            offset;

        const length =
            flowerData.petalSize *
            lengthRatio *
            flowerData.petalLengthFactor;

        const width =
            flowerData.petalSize *
            widthRatio *
            (1 + flowerData.petalWidthFactor);

        const wave =
            Math.sin(i * 1.7) *
            flowerData.waveFactor *
            20;

        const petalPath =
            createPetalPath(cx, cy, angle, length + wave, width, flowerData);

        fillHashInsideShape(petalPath, flowerData, i);
    }
}

function createPetalPath(cx, cy, angle, length, width, flowerData) {
    const baseX = cx + Math.cos(angle) * 18;
    const baseY = cy + Math.sin(angle) * 18;

    const tipX = cx + Math.cos(angle) * length;
    const tipY = cy + Math.sin(angle) * length;

    const sideAngle = angle + Math.PI / 2;

    const leftX = baseX + Math.cos(sideAngle) * width;
    const leftY = baseY + Math.sin(sideAngle) * width;

    const rightX = baseX - Math.cos(sideAngle) * width;
    const rightY = baseY - Math.sin(sideAngle) * width;

    const curvePower = 1.1 + flowerData.petalTipFactor;

    const curveX1 =
        cx +
        Math.cos(angle) * (length * 0.45) +
        Math.cos(sideAngle) * width * curvePower;

    const curveY1 =
        cy +
        Math.sin(angle) * (length * 0.45) +
        Math.sin(sideAngle) * width * curvePower;

    const curveX2 =
        cx +
        Math.cos(angle) * (length * 0.45) -
        Math.cos(sideAngle) * width * curvePower;

    const curveY2 =
        cy +
        Math.sin(angle) * (length * 0.45) -
        Math.sin(sideAngle) * width * curvePower;

    const path = new Path2D();

    path.moveTo(baseX, baseY);
    path.bezierCurveTo(leftX, leftY, curveX1, curveY1, tipX, tipY);
    path.bezierCurveTo(curveX2, curveY2, rightX, rightY, baseX, baseY);
    path.closePath();

    return path;
}

function fillHashInsideShape(path, flowerData, seed) {
    ctx.save();

    ctx.clip(path);

    ctx.font = "5px monospace";
    ctx.fillStyle = hexToRgba(flowerData.color, 0.82);

    const hash = flowerData.hashText;
    let index = seed * 17;

    for (let y = 0; y < canvas.height; y += 5) {
        for (let x = 0; x < canvas.width; x += 4) {
            if (ctx.isPointInPath(path, x, y)) {
                ctx.fillText(hash[index % hash.length], x, y);
                index++;
            }
        }
    }

    ctx.restore();
}

function drawCenter(flowerData, cx, cy) {
    ctx.font = "bold 6px monospace";
    ctx.fillStyle = "rgba(245,245,245,0.9)";

    const hash = flowerData.hashText;
    let index = 0;

    for (let y = cy - 18; y <= cy + 18; y += 6) {
        for (let x = cx - 18; x <= cx + 18; x += 5) {
            const distance = Math.sqrt((x - cx) ** 2 + (y - cy) ** 2);

            if (distance <= 20) {
                ctx.fillText(hash[index % hash.length], x, y);
                index++;
            }
        }
    }
}

function drawStemHash(flowerData, cx, cy) {
    ctx.font = "7px monospace";
    ctx.fillStyle = "rgba(120,180,130,0.7)";

    const hash = flowerData.hashText;
    let index = 0;

    const stemTopY = cy + 120;
    const stemBottomY = canvas.height - 40;
    const stemHalfWidth = 9;

    for (let y = stemTopY; y < stemBottomY; y += 7) {
        const sway =
            Math.sin((y - stemTopY) * 0.035) *
            flowerData.waveFactor *
            20;

        const centerX = cx + sway;

        for (let x = centerX - stemHalfWidth; x <= centerX + stemHalfWidth; x += 6) {
            ctx.fillText(hash[index % hash.length], x, y);
            index++;
        }
    }
}

function degreesToRadians(degrees) {
    return degrees * Math.PI / 180;
}

function hexToRgba(hex, alpha) {
    const r = parseInt(hex.substring(1, 3), 16);
    const g = parseInt(hex.substring(3, 5), 16);
    const b = parseInt(hex.substring(5, 7), 16);

    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}

idCardBtn.addEventListener("click", () => {
    window.location.href = "idcard.html";
});