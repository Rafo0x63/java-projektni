package com.prod.hydraulicsystemsmaintenance.threads;

import com.prod.hydraulicsystemsmaintenance.controllers.ChangesViewController;

public class SerializeChange implements Runnable {
    @Override
    public void run() {
        ChangesViewController.saveChanges();
    }
}