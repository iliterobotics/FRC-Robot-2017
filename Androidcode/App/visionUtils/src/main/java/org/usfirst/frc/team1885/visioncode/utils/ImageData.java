package org.usfirst.frc.team1885.visioncode.utils;

import java.io.*;
import java.util.*;

/**
 * Created by Atishay on 1/29/2017.
 */
public class ImageData implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7516225605314348147L;
    private double x;
    private double y;
    
    public double getX()
    {
        return x;
    }
    public void setX(double x)
    {
        this.x = x;
    }
    public double getY()
    {
        return y;
    }
    public void setY(double y)
    {
        this.y = y;
    }
    
    

}
