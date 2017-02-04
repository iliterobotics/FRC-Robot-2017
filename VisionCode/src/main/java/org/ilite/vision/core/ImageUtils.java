package org.ilite.vision.core;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.usfirst.frc.team1885.visioncode.utils.SimpleImage;

/**
 * Class containing utility methods to go back and forth between OPENCV and Java
 * 
 * @author Christopher
 * 
 */
public class ImageUtils {
    /**
     * Helper method to convert an OPENCV {@link Mat} to an {@link Image} If the
     * passed in image is a gray scale, the returned image will be gray. If the
     * passed in image is multi-channel, the return image is RGB
     * 
     * @param pMatrix
     *            The matrix to convert
     * @return The Image
     */
    public static BufferedImage toBufferedImage(SimpleImage pImage) {

        int type = BufferedImage.TYPE_BYTE_GRAY;
        int numChannels = pImage.getNumChannels();
        
        if (numChannels > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        byte[] b = pImage.getRawImage();
        
        BufferedImage image = new BufferedImage(pImage.getCols(), pImage.getRows(), type);
        
        image.getRaster().setDataElements(0, 0, pImage.getCols(), pImage.getRows(), b);
        
        return image;
    }
}
