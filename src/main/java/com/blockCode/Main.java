package com.blockCode;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    int resolutionX = 160;
    int resolutionY = 80;

    String lines[] = new String[] { "TEICH", "WASSER" };
    int fontSize = 32;

    int blockSize = 40;
    boolean quadBlockInit = true;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("Drawing Image....");

        boolean[][] blocks = new boolean[resolutionX][resolutionY];

        Random random = new Random(1);

        if (quadBlockInit)
            for (int x = 0; x < resolutionX; x += 2) {
                for (int y = 0; y < resolutionY; y += 2) {
                    switch (random.nextInt(6)) {
                        case 0:
                            blocks[x][y] = true;
                            blocks[x + 1][y + 1] = true;
                            break;
                        case 1:
                            blocks[x + 1][y] = true;
                            blocks[x][y + 1] = true;
                            break;
                        case 2:
                            blocks[x][y] = true;
                            blocks[x + 1][y] = true;
                            break;
                        case 3:
                            blocks[x][y + 1] = true;
                            blocks[x + 1][y + 1] = true;
                            break;
                        case 4:
                            blocks[x][y] = true;
                            blocks[x][y + 1] = true;
                            break;
                        case 5:
                            blocks[x + 1][y] = true;
                            blocks[x + 1][y + 1] = true;
                            break;
                    }
                }
            }
        else
            for (int x = 0; x < resolutionX; x++) {
                for (int y = 0; y < resolutionY; y++) {
                    blocks[x][y] = random.nextBoolean();
                }
            }

        printBlocks("first.svg", blocks);

        BufferedImage bImage = new BufferedImage(resolutionX, resolutionY,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bImage.createGraphics();
        g2.setFont(new Font("Pixolletta8px", Font.BOLD, fontSize));
        g2.setColor(Color.WHITE);
        {
            int y = resolutionY / 2 - fontSize / 2 * lines.length;

            for (String line : lines) {
                y += fontSize;
                FontMetrics metrics = g2.getFontMetrics(g2.getFont());
                int width = metrics.stringWidth(line);
                g2.drawString(line, (int) (resolutionX / 2 - width / 2), y);
            }
        }

        try {
            ImageIO.write(bImage, "png", new File("text.png"));
        } catch (IOException ex) {
            System.err.println(ex);
        }

        for (int x = 0; x < resolutionX; x++) {
            for (int y = 0; y < resolutionY; y++) {
                if ((bImage.getRGB(x, y) & 1) == 1) {
                    blocks[x][y] = !blocks[x][y];
                }
            }
        }

        printBlocks("second.svg", blocks);
    }

    private void printBlocks(String fileName, boolean[][] blocks) {
        double width = resolutionX * blockSize;
        double height = resolutionY * blockSize;
        SVGGraphics2D g2 = new SVGGraphics2D(width, height);
        double overlap = 1.2;
        g2.setColor(Color.BLACK);
        for (int i = 0; i < resolutionX; i++) {
            for (int j = 0; j < resolutionY; j++) {
                if (blocks[i][j]) {
                    g2.fillRect(i * blockSize, j * blockSize, (int) (overlap * blockSize),
                            (int) (overlap * blockSize));
                }
            }
        }

        File f = new File(fileName);
        try {
            SVGUtils.writeToSVG(f, g2.getSVGElement());
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}