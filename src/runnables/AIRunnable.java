package runnables;

import controller.Controller;

public class AIRunnable implements Runnable {

    public Boolean flag = true;
    public Controller c;

    public AIRunnable(Controller _c){
        c = _c;
    }

    public void setRunFlag(Boolean _f){
        flag = _f;
    }

    @Override
    public void run() {
        while(true) {
            if(flag == true)
                c.computerVsComputer();
            if(flag == true)
                c.getMainWindow().getBoardPanel().repaintFigures();
            if(flag == false)
                break;
        }
        c.getMainWindow().getBoardPanel().getAIButtonm().setEnabled(true);
        c.getMainWindow().getBoardPanel().getAIButtonm().setBackground(null);
        c.getMainWindow().getBoardPanel().enableFigures();

    }
}
