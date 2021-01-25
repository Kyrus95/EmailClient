package com.ejemplo.Kyosuke;

import java.io.*;
import java.net.*;
import java.util.*;
public class Main
{
    private static InetAddress host;
    private static final int PORT = 1234;
    private static String name;
    private static Scanner networkInput, userEntry;
    private static PrintWriter networkOutput;
    private static byte[] ip;

    public static void main(String[] args)
            throws IOException
    {
        try
        {
            //host = InetAddress.getLocalHost();
            ip = new byte[]{(byte) 172,20,0,117};
            host = InetAddress.getByAddress(ip);
        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID no encontrado!");
            System.exit(1);
        }
        userEntry = new Scanner(System.in);
        do
        {
            System.out.print("\nEnter name ('Kyo' or 'Guest'): ");
            name = userEntry.nextLine();
        }while (!name.equals("Kyo") && !name.equals("Guest"));
        talkToServer();
    }
    private static void talkToServer() throws IOException
    {
        String option = null, message, response;
        do
        {
/******************************************************
 CREATE A SOCKET, SET UP INPUT AND OUTPUT STREAMS,
 ACCEPT THE USER'S REQUEST, CALL UP THE APPROPRIATE
 METHOD (doSend OR doRead), CLOSE THE LINK AND THEN
 ASK IF USER WANTS TO DO ANOTHER READ/SEND.
 ******************************************************/
            Socket link = null;//Step 1.
            System.out.print("\nOpening connection");
            try{
                link = new Socket(host,PORT);//Step 1.
                System.out.print("\nConnection established");
                networkInput = new Scanner(link.getInputStream());//Step 2.
                networkOutput = new PrintWriter(link.getOutputStream(),true);//Step 2.
                //Set up stream for keyboard entry...
                Scanner userEntry = new Scanner(System.in);
                do
                {
                    System.out.print("\nEnter request ('Send' or 'Read'): ");
                    option = userEntry.nextLine();
                }while (!option.equals("Send") && !option.equals("Read"));
                if (option.equals("Send")){
                    doSend();
                }
                if (option.equals("Read")){
                    doRead();
                }
                System.out.print("\nWould you like to Send/Read again? ('Y' or 'N'): ");
                option = userEntry.nextLine();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    System.out.println("\n* Closing connection... *");
                    link.close();
                } catch (IOException e) {
                    System.out.println("Unable to disconnect");
                    System.exit(1);
                }
            }
        }while (!option.equals("N"));
    }
    private static void doSend()
    {

        System.out.println("\nEnter 1-line message: ");
        String message = userEntry.nextLine();
        //System.out.println(name+" "+message);
        networkOutput.println(name);
        networkOutput.println("Envia");
        networkOutput.println(message);
    }
    private static void doRead() throws IOException
    {
        System.out.println("\nMostrando mensajes...\n");
        networkOutput.println(name);
        networkOutput.println("Lee");
        String response = networkInput.nextLine();
        String[] inbox = new String[Integer.parseInt(response)];
        System.out.println("Cantidad de Mensajes recibidos: "+response);
        for (int i = 0; i<(Integer.parseInt(response)); i++){
            inbox[i] = networkInput.nextLine();
            System.out.println("Mensaje "+(i+1)+": "+inbox[i]);
        }
    }
}