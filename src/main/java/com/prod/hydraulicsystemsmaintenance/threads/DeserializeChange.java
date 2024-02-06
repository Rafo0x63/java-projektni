package com.prod.hydraulicsystemsmaintenance.threads;

import com.prod.hydraulicsystemsmaintenance.controllers.ChangesViewController;

public class DeserializeChange implements Runnable {
    @Override
    public void run() {
        ChangesViewController.getChanges();
    }
}
