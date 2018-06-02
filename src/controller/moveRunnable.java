package controller;

public class moveRunnable implements Runnable {
    public Controller c;
    moveRunnable(Controller _c){
        c = _c;
    }

    @Override
    public void run() {
        c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(false);
        c.makeComputerMove();
        c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(true);
    }
}
