package me.dumfing.mainmenu;

import com.badlogic.gdx.Gdx;
import me.dumfing.menutools.Menu;
import me.dumfing.menutools.MenuTools;
import me.dumfing.multishooter.GameState;
import me.dumfing.multishooter.MainShooter;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    public MainMenu(){

        //Play Button
        super.addButton(new MenuTools.Button(Gdx.graphics.getWidth()/2-20, Gdx.graphics.getHeight()/2-20, 40, 40, new MenuTools.OnClick() {
            @Override
            public void action() {
                System.out.println("Click!");
                MainShooter.state = GameState.SERVERBROWSER;
            }
        }));
    }
}
