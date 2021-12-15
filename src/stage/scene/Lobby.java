package stage.scene;

import component.animation.Animation;
import component.animation.Animator;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.MenuButton;
import stage.GameStage;
import stage.GameStageID;

import object.OthelloObject;
import graphics.Sprite;
import util.FontLibrary;

import java.awt.*;

public class Lobby extends OthelloObject implements GameStage {
//数据部分
    public static final double PopOutDuration = 1.0;
    public static final double PopBackDuration = 0.3;

    public static final double MenuButtonWidth = 280;
    public static final double MenuButtonHeight = 60;
    public static final Vct MainManuButtonsPivot = new Vct( MenuButtonWidth * 0.43,  mainApp.Height*0.30);
    public static final double MenuShift = MenuButtonWidth;
    private boolean readyForGame;
    private boolean exitFlag;


//数据部分结束

    //背景
    private OthelloObject background;
    //背景结束


    private enum MenuState {
        Main, SelectMode, Connect, Help, Room, Empty
    }
    private MenuState menuState;

    //MainMenu部分
    private OthelloObject menu_main;
    private MenuButton button_main_start;
    private MenuButton button_main_help;
    private MenuButton button_main_exit;
    private Animator menu_main_animator;
    private Animator menu_main_alphaAnimator;


    public Lobby() {
        super("scene_lobby");
    }

    @Override
    public void init() {
        //背景布部分
        this.background = new OthelloObject("background", new Sprite("background"));
        this.addObj(this.background);
        this.background.resizeTo(mainApp.WinSize);
        this.background.setPosition(mainApp.WinSize.x/2, mainApp.WinSize.y/2);

        initMainMenu();

        this.menu_main_setActive(false);
        this.menu_main_setActive(true);
        this.menu_main_popOut(0);
        this.menuState = MenuState.Main;
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        this.menu_main_update(dt);



        if (this.menuState == MenuState.Main) { // Main
            if (this.button_main_start.isClicked()) {
                System.out.println("Play被点击");
                this.readyForGame = true;
            }
//            if (this.button_main_exit.isClicked()) {
//                this.exitFlag = true;
//            }
        }

    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Lobby;
    }


    //主界面开始
    public void initMainMenu() {
        this.menu_main = new OthelloObject("menu_main");
        this.addObj(this.menu_main);
        this.menu_main.setPosition(MainManuButtonsPivot);
        this.menu_main.setAlpha(0);

        this.button_main_start = new MenuButton("menu_main_start", new Sprite("popo"));
        this.menu_main.addObj(this.button_main_start);

        //大小
        this.button_main_start.resizeTo(MenuButtonWidth, MenuButtonHeight);

        //位置
        this.button_main_start.setPosition(0,0);

        //文字显示
        Font font = FontLibrary.GetMenuButtonFont(30);
        this.button_main_start.setFont(font);
        this.button_main_start.setText("Play");
        this.button_main_start.setTextColor(new Color(212, 212, 212));

        this.menu_main_animator = new Animator(0);
        this.menu_main_alphaAnimator = new Animator(0);
        this.addComponent(this.menu_main_animator);
        this.addComponent(this.menu_main_alphaAnimator);


    }

    private void menu_main_popOut(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetTanh(0, 0, PopOutDuration, true, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetTanh(this.menu_main_alphaAnimator.val(), 1, PopOutDuration, true, delay));
    }

    private void menu_main_popBack(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetTanh(this.menu_main_animator.val(), MenuShift, PopBackDuration, false, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetTanh(this.menu_main_alphaAnimator.val(), 0, PopBackDuration, false, delay));
    }

    private void menu_main_update(double dt) {
        this.menu_main.setPosition(MainManuButtonsPivot.x + this.menu_main_animator.val(), MainManuButtonsPivot.y);
        this.menu_main.setAlpha(this.menu_main_alphaAnimator.val());
    }

    private void menu_main_setActive(boolean flag) {
        this.button_main_start.setActive(flag);
    }
    //主界面结束

    public boolean isExiting() {
        return exitFlag;
    }

    public boolean isReadyForGame() { return  readyForGame;}

}