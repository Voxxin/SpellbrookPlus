package com.github.voxxin.spellbrookplus.core.lifecycle;

public class Task {

    Runnable runnable;
    int wait;
    int pointer = 0;

    private Task(int wait, Runnable r) {
        this.wait = wait;
        this.runnable = r;
    }

    public void run() {
        if (this.pointer == this.wait) {
            this.runnable.run();
            this.pointer = 0;
        }
        else this.pointer++;
    }

    public static Task of(Runnable r, int wait) {
        return new Task(wait, r);
    }

}
