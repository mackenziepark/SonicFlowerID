const flowerButton =
    document.getElementById(
        "flowerButton"
    );

const transitionCover =
    document.getElementById(
        "transitionCover"
    );

flowerButton.addEventListener(
    "click",
    () => {

        flowerButton.style.transform =
            "scale(1.35) rotate(4deg)";

        transitionCover.classList.add(
            "active"
        );

        setTimeout(() => {

            window.location.href =
                "identity.html";

        }, 800);
    }
);