package com.example.christopher.myapplication;

import android.util.Log;

import org.opencv.core.Mat;
import org.usfirst.frc.team1885.visioncode.utils.ImageData;
import org.usfirst.frc.team1885.visioncode.utils.SimpleImage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Atishay on 1/29/2017.
 */

public class ImageServer {


    ImageServer() {

    }

    public void  connect() {
        final ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Name");
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                ServerSocket server = null;
                try {
                    server  = new ServerSocket(1180);
                } catch(IOException e){

                    e.printStackTrace();
                }

                while(server != null) {
                    try {
                        Socket accept = server.accept();
                        ClientThread clientThread = new ClientThread(accept);
                        allClients.add(clientThread);
                        service.submit(clientThread);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    public void submitImage(Mat aMat) {
        for(ClientThread aThread : allClients){
            aThread.submitImage(aMat);
        }
    }


    private final List<ClientThread>allClients = new ArrayList<>();


    private class ClientThread implements Runnable{
        private Socket clientSocket;
        private LinkedBlockingQueue<Mat>allImages = new LinkedBlockingQueue<>();
        private ObjectOutputStream objectOutputStream;

        ClientThread(Socket clientSocket) throws IOException{
            this.clientSocket = clientSocket;

            OutputStream output = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(output);
        }

        @Override
        public void run() {

            while(true) {
                List<Mat> imagesToSend = new ArrayList<>();
                allImages.drainTo(imagesToSend);

                if (!imagesToSend.isEmpty()) {
                    for (Mat aMat : imagesToSend) {
                        send(aMat);
                    }
                }
            }
        }

        public void submitImage(Mat pMat) {
            allImages.add(pMat);
        }


        private void send(Mat pMat)
        {
            try {
                SimpleImage img = new SimpleImage();
                img.setCols(pMat.cols());
                img.setRows(pMat.rows());
                img.setNumChannels(pMat.channels());

                byte[] raw = new byte[img.getCols() * img.getNumChannels() * img.getRows()];
                pMat.get(0, 0, raw );
                img.setRawImage(raw);

                objectOutputStream.writeObject(img);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        private void send(ImageData imageData)
        {

            try {


                objectOutputStream.writeObject(imageData);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private class ClienThreadForImageData implements Runnable
    {
        private Socket clientSocket;
        private LinkedBlockingQueue<Double>imageDatas = new LinkedBlockingQueue<>();
        private ObjectOutputStream objectOutputStream;

        ClientThreadForImageData(Socket clientSocket) throws IOException{
            this.clientSocket = clientSocket;

            OutputStream output = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(output);
        }

        @Override
        public void run() {

            while(true) {
                List<Double> imageDataToSend = new ArrayList<>();
                imageDatas.drainTo(imageDataToSend);

                if (!imageDataToSend.isEmpty()) {
                    for (Double d : imageDataToSend) {
                        send(d);
                    }
                }
            }
        }

        public void submitImage(double d) {
            imageDatas.add(d);
        }


        private void send(double d)
        {
            try {
                ImageData img = new ImageData();
                img(img);
                img.setRows(pMat.rows());
                img.setNumChannels(pMat.channels());

                byte[] raw = new byte[img.getCols() * img.getNumChannels() * img.getRows()];
                pMat.get(0, 0, raw );
                img.setRawImage(raw);

                objectOutputStream.writeObject(img);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        private void send(ImageData imageData)
        {

            try {


                objectOutputStream.writeObject(imageData);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
