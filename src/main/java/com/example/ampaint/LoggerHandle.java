package com.example.ampaint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Contains methods for log handling.
 */
public class LoggerHandle extends Thread {
    private static File fileLog;
    private static LoggerHandle log = new LoggerHandle();
    private static Queue<Runnable> runnableQueue = new LinkedBlockingQueue<Runnable>();

    /**
     * Gets the loggerhandle.
     * @return loggerhandle
     */
    public static LoggerHandle getLoggerHandle() {
        return log;
    }      // for use in other classes

    /**
     * Runs the thread.
     */
    public void run() {      // for running the thread
        try {
            if (runnableQueue.size() > 0)  // checks to make sure that there is actually something to run
                runnableQueue.remove().run(); // removes runnable from queue and runs
        }
        catch (Exception ex) {}
    }

    /**
     * Stops the thread.
     */
    public static void cease() {log.interrupt();}     // stop the current thread

    /**
     * Used to activate the threading in controller class.
     * @param args default argument
     */
    public static void main(String[] args) {
        try {
            String userHome = System.getProperty("user.home");    // gets user's home directory
            String dateTime = (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));     // i.e 2022-10-15
            String fileName = dateTime.substring(0,10);
            fileLog = new File(userHome + "/ampaint/logs/" + fileName + ".txt");    // makes folder using this format
            fileLog.getParentFile().mkdirs();
            fileLog.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error occurred.");
            ex.printStackTrace();
        }

        log.start();
        log.writeToLog(false, " New Log File: " + fileLog.getName());
    }

    /**
     * Adds a new log to the running thread.
     * @param addFileName the name of the file
     * @param text sentence
     */
    public void writeToLog(boolean addFileName, String text) {
        logWriter lw = new logWriter(addFileName, text);
        runnableQueue.add(lw);
        log.run();       // activates thread
    }

    /**
     * Allows adding to a thread.
     */
    class logWriter implements Runnable {
        private boolean addFileName;
        private String text;

        public logWriter(boolean aFN, String txt) {  // parameters from logWriter
            addFileName = aFN;
            text = txt;
        }

        /**
         * The "runnable" version of run().
         */
        @Override
        public void run() {
            try {
                FileWriter fWriter = new FileWriter(fileLog, true);              // writes text to file
                String dateTime = (LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")));
                fWriter.append(dateTime);
                if (addFileName) {
                    try {
                        fWriter.append(" (Untitled Canvas 1) ");
                    }
                    catch (IOException ex) {fWriter.append(" (Unsaved Canvas) ");}
                }
                fWriter.append(text + "\n");   // writes the above plus the text passed in as an arg
                fWriter.close();       // closes the file writer to be safe
            }
            catch (IOException ex) {
                System.out.println("Error occurred.");
                ex.printStackTrace();
            }
        }
    }
}