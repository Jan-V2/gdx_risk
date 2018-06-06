package Gdx_Risk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

public class FirstView extends AbstractLmlView {
    public FirstView() {
        super(new Stage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("test.lml");
    }

    @Override
    public String getViewId() {
        return "first";
    }
}