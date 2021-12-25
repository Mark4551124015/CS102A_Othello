package stage.scene;

import component.animation.Animation;
import component.animation.Animator;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.MenuButton;
import object.GUI.Buttons.NormalButton;
import object.GUI.Buttons.SelectModeButton;
import stage.GameStage;

import object.OthelloObject;
import graphics.Image;
import util.FontLibrary;

import java.awt.*;

public class Lobby extends OthelloObject implements GameStage {
    //数据部分
    public static final double PopOutDuration = 0.2;
    public static final double PopBackDuration = 0.2;

    public static final double MenuButtonWidth = 280;
    public static final double MenuButtonHeight = 60;
    public static final Vct MainManuButtonsPivot = new Vct(MenuButtonWidth * 0.43, mainApp.Height * 0.30);
    public static final double MenuShift = MenuButtonWidth;
    private boolean exitFlag;
    private boolean help;
    private boolean options;
    private boolean local;
    private boolean online;
    private boolean ai;
    private boolean back;

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
    private MenuButton button_main_options;
    private MenuButton button_main_exit;
    private Animator menu_main_animator;
    private Animator menu_main_alphaAnimator;
    //
//    //SelectModeMenu部分
    private OthelloObject menu_SelectMode;
    private SelectModeButton button_SelectMode_local;
    private SelectModeButton button_SelectMode_online;
    private SelectModeButton button_SelectMode_ai;
    private NormalButton button_SelectMode_back;
    private Animator menu_SelectMode_animator;
    private Animator menu_SelectMode_alphaAnimator;

    public Lobby() {
        super("scene_lobby");
    }

    @Override
    public void init() {
        //背景布部分
        this.background = new OthelloObject("background", new Image("background"));
        this.addObj(this.background);
        this.background.resizeTo(mainApp.WinSize);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);

        initMainMenu();
        initModeMenu();

        this.menu_main_setActive(true);
//        this.menu_SelectMode_setActive(true);
//        this.menuState = MenuState.SelectMode;
//        this.menu_SelectMode_popOut(0);
        this.menu_main_popOut(0);

        this.menuState = MenuState.Main;
    }

    @Override
    public void update(double dt) {

        this.menu_main_update(dt);
        this.menu_SelectMode_update(dt);


        if (this.menuState == MenuState.Main) { // MainTest
            if (this.button_main_start.isClicked()) {
                System.out.println("Play被点击");
                this.changeMenuState(MenuState.SelectMode);
            }
            if (this.button_main_exit.isClicked()) {
                this.exitFlag = true;
            }
            if (this.button_main_help.isClicked()) {
                this.help = true;
            }
            if (this.button_main_options.isClicked()) {
                this.options = true;
            }
        }

        if(this.menuState == MenuState.SelectMode){
            if(this.button_SelectMode_local.isClicked()){
                this.local = true;
            }
            if(this.button_SelectMode_online.isClicked()){
                this.online = true;
            }
            if(this.button_SelectMode_ai.isClicked()){
                this.ai = true;
            }
            if(this.button_SelectMode_back.isClicked()){
                this.changeMenuState(MenuState.Main);
            }
        }
        super.update(dt);






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

        this.button_main_start = new MenuButton("menu_main_start",null);
        this.menu_main.addObj(this.button_main_start);
        this.button_main_help = new MenuButton("menu_main_help",null);
        this.menu_main.addObj(this.button_main_help);
        this.button_main_options = new MenuButton("menu_main_options", null);
        this.menu_main.addObj(this.button_main_options);
        this.button_main_exit = new MenuButton("menu_main_exit", null);
        this.menu_main.addObj(this.button_main_exit);

        //大小
        this.button_main_start.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_main_help.resizeTo(MenuButtonWidth, MenuButtonHeight-7);
        this.button_main_options.resizeTo(MenuButtonWidth, MenuButtonHeight-7);
        this.button_main_exit.resizeTo(MenuButtonWidth, MenuButtonHeight-7);

        //位置
        this.button_main_start.setPosition(0, 50);
        this.button_main_help.setPosition(0, 125);
        this.button_main_options.setPosition(0, 180);
        this.button_main_exit.setPosition(0, 235);


        //文字显示
        Font font1 = FontLibrary.GetMenuButtonFont(30);
        this.button_main_start.setFont(font1);
        Font font2 = FontLibrary.GetMenuButtonFont(15);
        this.button_main_help.setFont(font2);
        this.button_main_options.setFont(font2);
        this.button_main_exit.setFont(font2);

        this.button_main_start.setText("Play    ");
        this.button_main_help.setText("Help              ");
        this.button_main_options.setText("Options         ");
        this.button_main_exit.setText("Exit Game      ");

        this.button_main_start.setTextColor(new Color(212, 212, 212));
        this.button_main_help.setTextColor(new Color(212, 212, 212));
        this.button_main_options.setTextColor(new Color(212, 212, 212));
        this.button_main_exit.setTextColor(new Color(212, 212, 212));


        this.menu_main_animator = new Animator(0);
        this.menu_main_alphaAnimator = new Animator(0);
        this.addComponent(this.menu_main_animator);
        this.addComponent(this.menu_main_alphaAnimator);

        this.menu_main_setActive(true);
    }

    private void menu_main_popOut(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetSmooth(-MenuShift, 0, PopOutDuration, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_main_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_main_popBack(double delay) {
        this.menu_main_animator.forceAppend(Animation.GetSmooth(this.menu_main_animator.val(), -MenuShift, PopBackDuration, delay));
        this.menu_main_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_main_alphaAnimator.val(), 0, PopBackDuration, delay));
    }

    private void menu_main_update(double dt) {
        this.menu_main.setPosition(MainManuButtonsPivot.x + this.menu_main_animator.val(), MainManuButtonsPivot.y);
        this.menu_main.setAlpha(this.menu_main_alphaAnimator.val());
    }

    private void menu_main_setActive(boolean flag) {
        this.button_main_start.setActive(flag);
        this.button_main_help.setActive(flag);
        this.button_main_options.setActive(flag);
        this.button_main_exit.setActive(flag);
    }
    //主界面结束



    public boolean isExiting() {
        return exitFlag;
    }

    public boolean isHelping() {
        return help;
    }

    public boolean isOptions() {
        return options;
    }

    public void initModeMenu() {
        this.menu_SelectMode = new OthelloObject("menu_SelectMode");
        this.addObj(this.menu_SelectMode);
        this.menu_SelectMode.setPosition(MainManuButtonsPivot);

        this.button_SelectMode_local = new SelectModeButton("menu_SelectMode_local", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_local);
        this.button_SelectMode_online = new SelectModeButton("menu_SelectMode_online", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_online);
        this.button_SelectMode_ai = new SelectModeButton("menu__SelectMode_ai", new Image("popo"));
        this.menu_SelectMode.addObj(this.button_SelectMode_ai);
        this.button_SelectMode_back = new NormalButton("menu_SelectMode_exitMenu");
        this.menu_SelectMode.addObj(this.button_SelectMode_back);

        //大小
        this.button_SelectMode_local.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_SelectMode_online.resizeTo(MenuButtonWidth, MenuButtonHeight);
        this.button_SelectMode_ai.resizeTo(MenuButtonWidth, MenuButtonHeight);

        //位置
        this.button_SelectMode_local.setPosition(200, 150);
        this.button_SelectMode_online.setPosition(500, 150);
        this.button_SelectMode_ai.setPosition(800, 150);
        this.button_SelectMode_back.setPosition(0, 400);

        //文字显示
        Font font1 = FontLibrary.GetMenuButtonFont(30);
        this.button_SelectMode_local.setFont(font1);
        this.button_SelectMode_online.setFont(font1);
        this.button_SelectMode_ai.setFont(font1);
        Font font2 = FontLibrary.GetMenuButtonFont(15);
        this.button_SelectMode_back.setFont(font2);

        this.button_SelectMode_local.setText("Local");
        this.button_SelectMode_online.setText("Online");
        this.button_SelectMode_ai.setText("Ai");
        this.button_SelectMode_back.setText("Back");

        this.button_SelectMode_local.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_online.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_ai.setTextColor(new Color(212, 212, 212));
        this.button_SelectMode_back.setTextColor(new Color(212, 212, 212));


        this.menu_SelectMode_animator = new Animator(0);
        this.menu_SelectMode_alphaAnimator = new Animator(0);
        this.menu_SelectMode.addComponent(this.menu_SelectMode_animator);
        this.menu_SelectMode.addComponent(this.menu_SelectMode_alphaAnimator);

        this.menu_SelectMode_setActive(false);

    }

    private void menu_SelectMode_popOut(double delay) {
        this.menu_SelectMode_animator.forceAppend(Animation.GetSmooth(MenuShift, 0, PopOutDuration, delay));
        this.menu_SelectMode_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 1, PopOutDuration, delay));
    }

    private void menu_SelectMode_popBack(double delay) {
        this.menu_SelectMode_animator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_animator.val(), MenuShift, PopBackDuration, delay));
        this.menu_SelectMode_alphaAnimator.forceAppend(Animation.GetSmooth(this.menu_SelectMode_alphaAnimator.val(), 0, PopBackDuration, delay));
    }

    private void menu_SelectMode_update(double dt) {
        this.menu_SelectMode.setPosition(MainManuButtonsPivot.x + this.menu_SelectMode_animator.val(), MainManuButtonsPivot.y);
        this.menu_SelectMode.setAlpha(this.menu_SelectMode_alphaAnimator.val());
    }

    private void menu_SelectMode_setActive(boolean flag) {
        this.button_SelectMode_local.setActive(flag);
        this.button_SelectMode_online.setActive(flag);
        this.button_SelectMode_ai.setActive(flag);
        this.button_SelectMode_back.setActive(flag);
    }

    public boolean ChosenLocal() {
        return this.local;
    }

    public boolean ChosenOnline() {
        return this.online;
    }

    public boolean ChosenAi() {
        return this.ai;
    }

    public boolean Back() {
        return this.back;
    }

    private void changeMenuState(MenuState nextState){
        switch (this.menuState) {
            case Main:
                this.menu_main_popBack(PopBackDuration);
                this.menu_main_setActive(false);
                break;
            case SelectMode:
                this.menu_SelectMode_popBack(PopBackDuration);
                this.menu_SelectMode_setActive(false);
                break;
            case Help:
                break;
        }
        switch (nextState){
            case Main:
                this.menu_main_popOut(PopOutDuration);
                this.menu_main_setActive(true);
                break;
            case SelectMode:
                this.menu_SelectMode_popOut(PopOutDuration);
                this.menu_SelectMode_setActive(true);
                break;
            case Help:
                break;
        }
        this.menuState = nextState;

    }


}