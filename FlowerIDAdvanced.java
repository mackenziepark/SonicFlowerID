import java.awt.*;
import java.awt.geom.GeneralPath;
import java.security.MessageDigest;
import javax.sound.sampled.*;
import javax.swing.*;


class AdvancedUserData {

    String name;
    String birthday;

    int voiceValue;
    double peakFrequency;

    AdvancedUserData(
            String name,
            String birthday,
            int voiceValue,
            double peakFrequency) {

        this.name = name;
        this.birthday = birthday;
        this.voiceValue = voiceValue;
        this.peakFrequency = peakFrequency;
    }
}

enum FlowerType {

    CHERRY_BLOSSOM,

    LOTUS,

    CHRYSANTHEMUM,

    CAMELLIA

}

class AdvancedFlowerData {

    Color color;

    FlowerType flowerType;

    int petalCount;
    int petalSize;

    double rotation;

    double petalLengthFactor;
    double petalWidthFactor;
    double petalTipFactor;
    double waveFactor;

    String hashText;

    AdvancedFlowerData(
            Color color,
            FlowerType flowerType,
            int petalCount,
            int petalSize,
            double rotation,
            double petalLengthFactor,
            double petalWidthFactor,
            double petalTipFactor,
            double waveFactor,
            String hashText) {

        this.color = color;

        this.flowerType = flowerType;

        this.petalCount = petalCount;
        this.petalSize = petalSize;

        this.rotation = rotation;

        this.petalLengthFactor = petalLengthFactor;
        this.petalWidthFactor = petalWidthFactor;
        this.petalTipFactor = petalTipFactor;
        this.waveFactor = waveFactor;

        this.hashText = hashText;
    }
}

class AdvancedVoiceFeature {

    int volumeValue;

    double peakFrequency;

    AdvancedVoiceFeature(
            int volumeValue,
            double peakFrequency) {

        this.volumeValue = volumeValue;
        this.peakFrequency = peakFrequency;
    }
}

class AdvancedFlowerGenerator {

    static AdvancedFlowerData generate(
            AdvancedUserData userData) {

        String normalizedName =
        userData.name
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "");

String normalizedBirthday =
        userData.birthday
                .trim()
                .replaceAll("[^0-9]", "");

String text =
        normalizedName
        + normalizedBirthday;

String sha256 =
        makeSHA256(text);

        int birthMonth =
                getBirthMonth(
                        userData.birthday);

        Color[] palette =
                getSeasonPalette(
                        birthMonth);

        int colorIndex =
                Integer.parseInt(
                        sha256.substring(0, 2),
                        16)
                % palette.length;

        Color color =
                palette[colorIndex];

        FlowerType flowerType =
                getFlowerTypeFromMonth(birthMonth);

        int petalCount =
                5
                + Integer.parseInt(
                        sha256.substring(2, 4),
                        16)
                % 6;

        int petalSize =
                70
                + userData.voiceValue;

        double rotation =
                Integer.parseInt(
                        sha256.substring(4, 8),
                        16)
                % 360;

        double peak =
                userData.peakFrequency;

        double petalLengthFactor =
                0.90
                + (peak % 300)
                / 300.0
                * 0.35;

        double petalWidthFactor =
                0.05
                + (peak % 180)
                / 180.0
                * 0.25;

        double petalTipFactor =
                0.05
                + (peak % 220)
                / 220.0
                * 0.35;

        double waveFactor =
                0.03
                + (peak % 300)
                / 300.0
                * 0.12;

        return new AdvancedFlowerData(
        color,
        flowerType,
        petalCount,
        petalSize,
        rotation,
        petalLengthFactor,
        petalWidthFactor,
        petalTipFactor,
        waveFactor,
        sha256
);
    }

    static String makeSHA256(String input) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    md.digest(input.getBytes());

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(
                        String.format("%02x", b)
                );
            }

            return sb.toString();

        } catch (Exception e) {
            return "00000000";
        }
    }

    static int getBirthMonth(String birthday) {
        try {
            if (birthday.length() >= 6) {
                return Integer.parseInt(
                        birthday.substring(4, 6)
                );
            }
        } catch (Exception e) {
            return 1;
        }

        return 1;
    }

    static Color[] getSeasonPalette(int month) {

        if (month == 3 || month == 4 || month == 5) {

            return new Color[] {
                    Color.decode("#DE8A8A"),
                    Color.decode("#E7D74B"),
                    Color.decode("#5F9E4B"),
                    Color.decode("#5492DB")
            };

        } else if (month == 6 || month == 7 || month == 8) {

            return new Color[] {
                    Color.decode("#DCA1C3"),
                    Color.decode("#80BC96"),
                    Color.decode("#94C7E6"),
                    Color.decode("#727FBD")
            };

        } else if (month == 9 || month == 10 || month == 11) {

            return new Color[] {
                    Color.decode("#BA271E"),
                    Color.decode("#D27840"),
                    Color.decode("#3D4531"),
                    Color.decode("#3B0E33")
            };

        } else {

            return new Color[] {
                    Color.decode("#CD3F85"),
                    Color.decode("#76154F"),
                    Color.decode("#286064"),
                    Color.decode("#110664")
            };
        }
    }

    static FlowerType getFlowerTypeFromMonth(
        int month) {

    if (month == 3 || month == 4 || month == 5) {
        return FlowerType.CHERRY_BLOSSOM;

    } else if (month == 6 || month == 7 || month == 8) {
        return FlowerType.LOTUS;

    } else if (month == 9 || month == 10 || month == 11) {
        return FlowerType.CHRYSANTHEMUM;

    } else {
        return FlowerType.CAMELLIA;
    }
 }
}

class AdvancedFlowerDrawingPanel extends JPanel {

    private AdvancedFlowerData flowerData;
    private double progress = 0;

    public AdvancedFlowerDrawingPanel() {
        setBackground(new Color(15, 15, 18));
    }

    public void setFlowerData(AdvancedFlowerData data) {
        this.flowerData = data;
        this.progress = 0;
        repaint();
    }

    public void startAnimation() {
        progress = 0;

        Timer timer = new Timer(30, e -> {
            progress += 0.02;

            if (progress >= 1) {
                progress = 1;
                ((Timer) e.getSource()).stop();
            }

            repaint();
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (flowerData != null) {
            drawFlower(g);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.setColor(new Color(230, 230, 230));

        String label = getFlowerLabel();
g2.drawString(label, getWidth() / 2 - 170, getHeight() - 45);

    }

    private String getFlowerLabel() {

        if (flowerData == null) {

            return "🌸 Sonic Flower ID";

        }

        switch (flowerData.flowerType) {

            case CHERRY_BLOSSOM:

                return "🌸 Sonic Flower ID";

            case LOTUS:

                return "🪷 Sonic Flower ID";

            case CHRYSANTHEMUM:

                return "🏵 Sonic Flower ID";

            case CAMELLIA:

                return "🌺 Sonic Flower ID";

            default:

                return "🌸 Sonic Flower ID";

        }

    }

    private void drawFlower(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int cx = getWidth() / 2;

        int cy = getHeight() / 2 - 20;

        g2.setRenderingHint(

                RenderingHints.KEY_ANTIALIASING,

                RenderingHints.VALUE_ANTIALIAS_ON

        );

drawStemHash(g2, cx, cy);

switch (flowerData.flowerType) {
    case CHERRY_BLOSSOM:
        drawCherryBlossomHash(g2, cx, cy);
        break;

    case LOTUS:
        drawLotusHash(g2, cx, cy);
        break;

    case CHRYSANTHEMUM:
        drawChrysanthemumHash(g2, cx, cy);
        break;

    case CAMELLIA:
        drawCamelliaHash(g2, cx, cy);
        break;
}

drawCenter(g2, cx, cy);

    }


    private void drawCherryBlossomHash(Graphics2D g2, int cx, int cy) {

    drawHashPetalLayer(g2, cx, cy, 5, 1.00, 0.75, 0);

}

    private void drawLotusHash(Graphics2D g2, int cx, int cy) {

    drawHashPetalLayer(g2, cx, cy, 8, 1.25, 0.45, 0);

    drawHashPetalLayer(g2, cx, cy, 6, 0.85, 0.35, Math.PI / 8);

}
    private void drawChrysanthemumHash(Graphics2D g2, int cx, int cy) {

    drawHashPetalLayer(g2, cx, cy, flowerData.petalCount * 3, 1.05, 0.22, 0);

}

    private void drawCamelliaHash(Graphics2D g2, int cx, int cy) {

    drawHashPetalLayer(g2, cx, cy, 7, 1.00, 0.75, 0);

    drawHashPetalLayer(g2, cx, cy, 6, 0.75, 0.60, Math.PI / 7);

}

private void drawHashPetalLayer(

        Graphics2D g2,

        int cx,

        int cy,

        int petals,

        double lengthRatio,

        double widthRatio,

        double offset) {

    int visiblePetals = (int)(petals * progress);

    for (int i = 0; i < visiblePetals; i++) {

        double angle =

                (2 * Math.PI / petals) * i

                + Math.toRadians(flowerData.rotation)

                + offset;

        double length =

                flowerData.petalSize

                * lengthRatio

                * flowerData.petalLengthFactor;

        double width =

                flowerData.petalSize

                * widthRatio

                * (1 + flowerData.petalWidthFactor);

        double wave =

                Math.sin(i * 1.7)

                * flowerData.waveFactor

                * 20;

        Shape petal =

                createPetalShape(

                        cx,

                        cy,

                        angle,

                        length + wave,

                        width

                );

        fillHashInsideShape(g2, petal, i);

    }

}

private Shape createPetalShape(

        int cx,

        int cy,

        double angle,

        double length,

        double width) {

    double baseX = cx + Math.cos(angle) * 18;

    double baseY = cy + Math.sin(angle) * 18;

    double tipX = cx + Math.cos(angle) * length;

    double tipY = cy + Math.sin(angle) * length;

    double sideAngle = angle + Math.PI / 2;

    double leftX = baseX + Math.cos(sideAngle) * width;

    double leftY = baseY + Math.sin(sideAngle) * width;

    double rightX = baseX - Math.cos(sideAngle) * width;

    double rightY = baseY - Math.sin(sideAngle) * width;

    double curvePower = 1.1 + flowerData.petalTipFactor;

    double curveX1 =

            cx

            + Math.cos(angle) * (length * 0.45)

            + Math.cos(sideAngle) * width * curvePower;

    double curveY1 =

            cy

            + Math.sin(angle) * (length * 0.45)

            + Math.sin(sideAngle) * width * curvePower;

    double curveX2 =

            cx

            + Math.cos(angle) * (length * 0.45)

            - Math.cos(sideAngle) * width * curvePower;

    double curveY2 =

            cy

            + Math.sin(angle) * (length * 0.45)

            - Math.sin(sideAngle) * width * curvePower;

    GeneralPath petal = new GeneralPath();

    petal.moveTo(baseX, baseY);

    petal.curveTo(leftX, leftY, curveX1, curveY1, tipX, tipY);

    petal.curveTo(curveX2, curveY2, rightX, rightY, baseX, baseY);

    petal.closePath();

    return petal;

}


private void fillHashInsideShape(

        Graphics2D g2,

        Shape shape,

        int seed) {

    Rectangle bounds = shape.getBounds();

    Shape oldClip = g2.getClip();

    Font oldFont = g2.getFont();

    g2.setClip(shape);

    g2.setFont(new Font("Monospaced", Font.PLAIN, 5));

    g2.setColor(new Color(

            flowerData.color.getRed(),

            flowerData.color.getGreen(),

            flowerData.color.getBlue(),

            210

    ));

    String hash = flowerData.hashText;

    int index = seed * 17;

    for (int y = bounds.y; y < bounds.y + bounds.height; y += 5) {

        for (int x = bounds.x; x < bounds.x + bounds.width; x += 4) {

            if (shape.contains(x, y)) {

                char c = hash.charAt(index % hash.length());

                g2.drawString(

                        String.valueOf(c),

                        x,

                        y

                );

                index++;

            }

        }

    }

    g2.setClip(oldClip);

    g2.setFont(oldFont);

}
       

    private void drawCenter(Graphics2D g2, int cx, int cy) {

        if (flowerData == null) {

            return;

        }

        g2.setColor(new Color(245, 245, 245, 230));

        g2.setFont(new Font("Monospaced", Font.BOLD, 6));

        String hash = flowerData.hashText;

        int index = 0;

        for (int y = cy - 18; y <= cy + 18; y += 6) {

            for (int x = cx - 18; x <= cx + 18; x += 5) {

                double distance =

                        Math.sqrt(

                                Math.pow(x - cx, 2)

                                + Math.pow(y - cy, 2)

                        );

                if (distance <= 20) {

                    char c =

                            hash.charAt(

                                    index % hash.length()

                            );

                    g2.drawString(

                            String.valueOf(c),

                            x,

                            y

                    );

                    index++;

                }

            }

        }

    }


    private void drawStemHash(Graphics2D g2, int cx, int cy) {
    if (flowerData == null) {
        return;
    }

    String hash = flowerData.hashText;

    Font oldFont = g2.getFont();

    g2.setFont(new Font("Monospaced", Font.PLAIN, 7));

    Color stemColor = new Color(120, 180, 130, 180);
    g2.setColor(stemColor);

    int index = 0;

    int stemTopY = cy + 25;
    int stemBottomY = getHeight() - 90;

    int stemHalfWidth = 9;

    for (int y = stemTopY; y < stemBottomY; y += 7) {

        double sway =
                Math.sin((y - stemTopY) * 0.035)
                * flowerData.waveFactor
                * 20;

        int centerX =
                cx
                + (int)sway;

        for (int x = centerX - stemHalfWidth; x <= centerX + stemHalfWidth; x += 6) {

            char c =
                    hash.charAt(
                            index % hash.length()
                    );

            g2.drawString(
                    String.valueOf(c),
                    x,
                    y
            );

            index++;
        }
    }

    g2.setFont(oldFont);
}
}
       

public class FlowerIDAdvanced {

    private JFrame frame;
    private JTextField nameField;
    private JTextField birthdayField;
    private JTextField voiceField;

    private AdvancedFlowerDrawingPanel flowerPanel;

    private double peakFrequency = 200;

    public static void main(String[] args) {
        new FlowerIDAdvanced().createGUI();
    }

    public void createGUI() {
        frame = new JFrame("🌸 Sonic Flower ID Generator");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Color backgroundColor = new Color(255, 245, 250);
        Color panelColor = new Color(255, 225, 235);
        Color buttonColor = new Color(255, 250, 252);
        Color sentenceColor = new Color(255, 200, 215);
        Color textColor = new Color(90, 55, 70);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 30, 15));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 20, 35));

        nameField = new JTextField();
        birthdayField = new JTextField();
        voiceField = new JTextField();

        nameField.setHorizontalAlignment(JTextField.CENTER);
        birthdayField.setHorizontalAlignment(JTextField.CENTER);
        voiceField.setHorizontalAlignment(JTextField.CENTER);

        JButton recordButton = new JButton("🎤 Record Voice (5 sec)");
        JButton generateButton = new JButton("🌷 Generate Flower");

        JLabel nameLabel = new JLabel("🌼 Name:");
        JLabel birthdayLabel = new JLabel("🎂 Birthday (YYYYMMDD):");
        JLabel voiceLabel = new JLabel("🎧 Voice Value:");
        JLabel sentenceLabel = new JLabel("🗣 Say this sentence:");

        JLabel sentenceBox =
                new JLabel("\"Hello, this is my new sonic flower ID.\"",
                        SwingConstants.CENTER);

        sentenceBox.setForeground(textColor);
        sentenceBox.setOpaque(true);
        sentenceBox.setBackground(sentenceColor);
        sentenceBox.setBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 160, 185), 1));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        inputPanel.add(birthdayLabel);
        inputPanel.add(birthdayField);

        inputPanel.add(voiceLabel);
        inputPanel.add(voiceField);

        inputPanel.add(recordButton);
        inputPanel.add(generateButton);

        inputPanel.add(sentenceLabel);
        inputPanel.add(sentenceBox);

        flowerPanel = new AdvancedFlowerDrawingPanel();
        flowerPanel.setBackground(backgroundColor);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(flowerPanel, BorderLayout.CENTER);

        recordButton.addActionListener(e -> {
            recordButton.setText("Recording... 5 sec");
            recordButton.setEnabled(false);

            new Thread(() -> {
                AdvancedVoiceFeature feature = recordVoice();

                SwingUtilities.invokeLater(() -> {
                    voiceField.setText(String.valueOf(feature.volumeValue));
                    peakFrequency = feature.peakFrequency;

                    recordButton.setText("🎤 Record Voice (5 sec)");
                    recordButton.setEnabled(true);
                });
            }).start();
        });

        generateButton.addActionListener(e -> generateFlower());

        frame.setVisible(true);
    }
private void generateFlower() {

    String name = nameField.getText();
    String birthday = birthdayField.getText();

    int voiceValue;

    try {
        voiceValue = Integer.parseInt(voiceField.getText());
    } catch (Exception e) {
        voiceValue = 50;
    }

    AdvancedUserData userData =
            new AdvancedUserData(
                    name,
                    birthday,
                    voiceValue,
                    peakFrequency);

    AdvancedFlowerData flowerData =
            AdvancedFlowerGenerator.generate(userData);

    flowerPanel.setFlowerData(flowerData);
    flowerPanel.startAnimation();

    
}

    private AdvancedVoiceFeature recordVoice() {
        try {
            AudioFormat format =
                    new AudioFormat(
                            44100,
                            16,
                            1,
                            true,
                            true);

            TargetDataLine line =
                    AudioSystem.getTargetDataLine(format);

            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            double[] samples = new double[262144];

            long sum = 0;
            int count = 0;
            int sampleIndex = 0;

            long start =
                    System.currentTimeMillis();

            while (System.currentTimeMillis() - start < 5000) {
                int bytesRead =
                        line.read(buffer, 0, buffer.length);

                for (int i = 0; i < bytesRead - 1; i += 2) {
                    int sample =
                            (buffer[i] << 8)
                            | (buffer[i + 1] & 0xFF);

                    sum += Math.abs(sample);
                    count++;

                    if (sampleIndex < samples.length) {
                        samples[sampleIndex] =
                                sample / 32768.0;

                        sampleIndex++;
                    }
                }
            }

            line.stop();
            line.close();

            if (count == 0) {
                return new AdvancedVoiceFeature(50, 200);
            }

            int avg = (int)(sum / count);

            int volumeValue =
                    Math.max(
                            1,
                            Math.min(100, avg / 50));

            double peak =
                    findPeakFrequency(
                            samples,
                            sampleIndex,
                            44100);

            return new AdvancedVoiceFeature(
                    volumeValue,
                    peak);

        } catch (Exception e) {
            e.printStackTrace();
            return new AdvancedVoiceFeature(50, 200);
        }
    }

    private double findPeakFrequency(
            double[] input,
            int sampleCount,
            double sampleRate) {

        int n = 1;

        while (n < sampleCount) {
            n *= 2;
        }

        double[] real = new double[n];
        double[] imag = new double[n];

        for (int i = 0; i < sampleCount; i++) {
            real[i] = input[i];
            imag[i] = 0;
        }

        fft(real, imag);

        int minBin = (int)(80 * n / sampleRate);
        int maxBin = (int)(1000 * n / sampleRate);

        double maxMagnitude = 0;
        int peakIndex = minBin;

        for (int i = minBin; i <= maxBin && i < n / 2; i++) {
            double magnitude =
                    real[i] * real[i]
                    + imag[i] * imag[i];

            if (magnitude > maxMagnitude) {
                maxMagnitude = magnitude;
                peakIndex = i;
            }
        }

        return peakIndex * sampleRate / n;
    }

    private void fft(double[] real, double[] imag) {
        int n = real.length;

        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;

            while (j >= bit) {
                j -= bit;
                bit >>= 1;
            }

            j += bit;

            if (i < j) {
                double tempReal = real[i];
                real[i] = real[j];
                real[j] = tempReal;

                double tempImag = imag[i];
                imag[i] = imag[j];
                imag[j] = tempImag;
            }
        }

        for (int len = 2; len <= n; len <<= 1) {
            double angle = -2 * Math.PI / len;

            double wLenReal = Math.cos(angle);
            double wLenImag = Math.sin(angle);

            for (int i = 0; i < n; i += len) {
                double wReal = 1;
                double wImag = 0;

                for (int j = 0; j < len / 2; j++) {
                    int u = i + j;
                    int v = i + j + len / 2;

                    double vReal =
                            real[v] * wReal
                            - imag[v] * wImag;

                    double vImag =
                            real[v] * wImag
                            + imag[v] * wReal;

                    real[v] = real[u] - vReal;
                    imag[v] = imag[u] - vImag;

                    real[u] += vReal;
                    imag[u] += vImag;

                    double nextWReal =
                            wReal * wLenReal
                            - wImag * wLenImag;

                    double nextWImag =
                            wReal * wLenImag
                            + wImag * wLenReal;

                    wReal = nextWReal;
                    wImag = nextWImag;
                }
            }
        }
    }
}
