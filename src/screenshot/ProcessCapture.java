/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenshot;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author savan
 */
public class ProcessCapture extends Thread {

    public String homeDirectory = "";
    public int nextRandomMinute = 0;

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public void run() {
        try {
            System.out.println("Start/Resuming");
            while (true) {
                this.nextRandomMinute = this.getNextRandomMinute();
                System.err.println("Sleeping for: " + this.nextRandomMinute);
                this.capture();
                this.sleep(this.nextRandomMinute * 60 * 1000);
            }
        } catch (Exception ex) {
        }
    }

    /**
     *
     * @return
     */
    public int getNextRandomMinute() {
        Random random = new Random();
        int min = 1;
        int max = 1;
        int randomNumber = random.nextInt((max - min) + 1) + min;
        return randomNumber;
    }

    /**
     *
     * @throws Exception
     */
    public void capture() throws Exception {
        if (this.homeDirectory.isEmpty()) {
            String osName = System.getProperty("os.name");
            this.getOSPath(osName);
        }
        String homeDirectory = this.homeDirectory;
        SimpleDateFormat dirFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat imgFormatter = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss_a");
        Calendar now = Calendar.getInstance();
        homeDirectory += "/" + dirFormatter.format(now.getTime());
        File file = new File(homeDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        Robot robot = new Robot();
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenShot, "JPG", new File(homeDirectory + "/" + imgFormatter.format(now.getTime()) + ".jpg"));
    }
    
    /**
     *
     * @param osName
     * @return
     */
    public String getOSPath(String osName) {
        osName = osName.toLowerCase();
        String path = "";
        if (osName.indexOf("windows") > -1) {
            path = System.getenv("APPDATA");
        } else if (osName.indexOf("mac") > -1) {
            path = System.getenv("?");
        } else {
            path = System.getProperty("user.home");
        }
        File rootOfPath = new File(path);
        this.homeDirectory = rootOfPath + "/Daily_Screen_Shots";
        File file = new File(this.homeDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        return this.homeDirectory;
    }

}
