package fr.an.test.eclipsemirror;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class SteganoDecodeMain {

    private File inputDir;
    private String inputFileBaseName;
    private String inputFileExt = ".png";
    private File outputDir;
    private String outputFilename;
    
    public static void main(String[] args) {
        SteganoDecodeMain app = new SteganoDecodeMain();
        app.parseArgs(args);
        app.run();
    }
    
    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.equals("-i")) {
                inputFileBaseName = args[++i];
            } else if (a.equals("--inputFileExt")) {
                inputFileExt = args[++i];
            } else if (a.equals("-d")) {
                inputDir = new File(args[++i]);
            } else if (a.equals("-o")) {
                outputFilename = args[++i];
            } else if (a.equals("--outputDir")) {
                outputDir = new File(args[++i]);
            }
        }
        if (inputDir == null) {
            inputDir = new File(".");
        }
        if (inputFileBaseName == null) {
            inputFileBaseName = "img";
        }
        if (outputDir == null) {
            outputDir = inputDir;
        }
        if (outputFilename == null) {
            outputFilename = "out";
        }
    }
    
    public void run() {
        for (int i = 0; ; i++) {
            File imgFile = new File(inputDir, inputFileBaseName + "-" + i + inputFileExt);
            if (! imgFile.exists()) {
                break;
            }

            try {
                // read(decode) img file
                BufferedImage img;
                try {
                    img = ImageIO.read(imgFile);
                } catch (IOException ex) {
                    throw new RuntimeException("Failed to read file " + imgFile, ex);
                }
    
                // copy byte[] content from png to dest zip
                DataBufferByte imgDataBuffer = (DataBufferByte) img.getRaster().getDataBuffer();
                byte[] imgData = imgDataBuffer.getData();
                DataInputStream din = new DataInputStream(new ByteArrayInputStream(imgData));
                int fileLen = din.readInt();
    
                File zipFile = new File(outputDir, outputFilename + "-" + i + ".zip");
                try (OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(zipFile))) {
                    fileOut.write(imgData, 4, fileLen);
                }
                
                // TODO unzip on the fly..
                
            } catch(Exception ex) {
                throw new RuntimeException("Failed", ex);
            }
        }
    }
}
