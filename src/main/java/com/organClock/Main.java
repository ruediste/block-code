package com.organClock;

import java.io.File;
import java.io.IOException;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGHints;
import org.jfree.svg.SVGUtils;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class Main {
    public static void main(String[] args) {
        System.out.println("Drawing Image....");
        double radius = 300;
        SVGGraphics2D g2 = new SVGGraphics2D(2 * radius, 2 * radius);
        g2.setRenderingHint(SVGHints.KEY_DRAW_STRING_TYPE, SVGHints.VALUE_DRAW_STRING_TYPE_VECTOR);
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 19));

        var words = new String[] { "Herz", "Dünndarm", "Blase", "Niere", "Kreislauf", "3-Erwärmer",
                "Gallenblase",
                "Leber", "Lunge", "Dickdarm", "Magen", "Milz-Pankreas" };

        var leftLine = new Line2D.Double(radius, radius, radius - radius * Math.sin(Math.PI * 2 / 24),
                radius - radius * Math.cos(Math.PI * 2 / 24));

        for (int i = 0; i < 12; i++) {
            var transform = g2.getTransform();
            g2.rotate(i * Math.PI * 2 / 12, radius, radius);

            // var rightLine = new Line2D.Double(radius, radius, radius + radius *
            // Math.sin(Math.PI * 2 / 24),
            // radius - radius * Math.cos(Math.PI * 2 / 24));
            var topLine = new Line2D.Double(
                    radius - radius * Math.sin(Math.PI * 2 / 24),
                    radius - radius * Math.cos(Math.PI * 2 / 24),
                    radius + radius * Math.sin(Math.PI * 2 / 24),
                    radius - radius * Math.cos(Math.PI * 2 / 24));
            var yellowPath = new Path2D.Double();
            yellowPath.append(leftLine, false);
            yellowPath.append(topLine, true);
            yellowPath.closePath();

            var r1 = radius - 35;
            var r2 = r1 - 20;
            Path2D.Double colorPath = new Path2D.Double();
            colorPath.moveTo(radius - r1 * Math.sin(Math.PI * 2 / 24), radius - r1 * Math.cos(Math.PI * 2 / 24));
            colorPath.lineTo(radius + r1 * Math.sin(Math.PI * 2 / 24), radius - r1 * Math.cos(Math.PI * 2 / 24));
            colorPath.lineTo(radius + r2 * Math.sin(Math.PI * 2 / 24), radius - r2 * Math.cos(Math.PI * 2 / 24));
            colorPath.append(
                    new Arc2D.Double(radius - r2, radius - r2, 2 * r2, 2 * r2, 90 - 360 / 24, 360 / 12,
                            Arc2D.OPEN),
                    true);
            colorPath.closePath();

            g2.setColor(new Color(255, 255, 200));
            g2.fill(yellowPath);
            g2.setColor((i + 1) / 2 % 2 == 0 ? Color.blue : Color.red);
            g2.fill(colorPath);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.draw(topLine);

            FontMetrics metrics = g2.getFontMetrics(g2.getFont());
            int width = metrics.stringWidth(words[i]);
            if (i > 2 && i < 9) {

                g2.translate(radius, (radius + r1) / 2);
                g2.rotate(Math.PI);
                g2.drawString(words[i], (int) -width / 2, (int) r1 - 5);
            } else {
                g2.drawString(words[i], (int) radius - width / 2, (int) (radius - r1 + 1));
            }

            g2.setTransform(transform);
        }

        for (int i = 0; i < 12; i++) {
            var transform = g2.getTransform();
            g2.rotate(i * Math.PI * 2 / 12, radius, radius);

            g2.setColor(Color.BLACK);
            g2.setStroke(
                    i % 4 == 0 ? new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL) : new BasicStroke(1));
            g2.draw(leftLine);
            g2.setTransform(transform);
        }

        File f = new File("image.svg");
        try {
            SVGUtils.writeToSVG(f, g2.getSVGElement());
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}