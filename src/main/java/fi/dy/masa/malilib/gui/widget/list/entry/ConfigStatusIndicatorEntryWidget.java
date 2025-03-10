package fi.dy.masa.malilib.gui.widget.list.entry;

import fi.dy.masa.malilib.gui.util.ScreenContext;
import fi.dy.masa.malilib.gui.widget.button.GenericButton;
import fi.dy.masa.malilib.gui.widget.button.OnOffButton;
import fi.dy.masa.malilib.gui.widget.list.DataListWidget;
import fi.dy.masa.malilib.overlay.widget.ConfigStatusIndicatorContainerWidget;
import fi.dy.masa.malilib.overlay.widget.sub.BaseConfigStatusIndicatorWidget;
import fi.dy.masa.malilib.render.ShapeRenderUtils;

public class ConfigStatusIndicatorEntryWidget extends BaseOrderableListEditEntryWidget<BaseConfigStatusIndicatorWidget<?>>
{
    protected final ConfigStatusIndicatorContainerWidget containerWidget;
    protected final GenericButton toggleButton;
    protected final GenericButton configureButton;
    protected final GenericButton removeButton;

    public ConfigStatusIndicatorEntryWidget(int x, int y, int width, int height,
                                            int listIndex, int originalListIndex,
                                            BaseConfigStatusIndicatorWidget<?> data,
                                            DataListWidget<BaseConfigStatusIndicatorWidget<?>> listWidget,
                                            ConfigStatusIndicatorContainerWidget containerWidget)
    {
        super(x, y, width, height, listIndex, originalListIndex, data, listWidget);

        this.useAddButton = false;
        this.useRemoveButton = false;
        this.useMoveButtons = false;
        this.containerWidget = containerWidget;

        this.toggleButton = OnOffButton.simpleSlider(16, this.data::isEnabled, this.data::toggleEnabled);
        this.configureButton = GenericButton.simple(16, "malilib.gui.button.label.configure", this::openEditScreen);
        this.removeButton = GenericButton.simple(16, "malilib.gui.button.label.remove", this::removeInfoRendererWidget);

        this.setText(data.getStyledName());
        this.getBackgroundRenderer().getNormalSettings().setEnabled(true);
    }

    public void removeInfoRendererWidget()
    {
        this.containerWidget.removeWidget(this.data);
        this.listWidget.refreshEntries();
    }

    public void openEditScreen()
    {
        this.getData().openEditScreen();
    }

    @Override
    public void reAddSubWidgets()
    {
        super.reAddSubWidgets();

        this.addWidget(this.toggleButton);
        this.addWidget(this.configureButton);
        this.addWidget(this.removeButton);
    }

    @Override
    protected void updateSubWidgetsToGeometryChangesPre(int x, int y)
    {
        super.updateSubWidgetsToGeometryChangesPre(x, y);

        x = this.getX();
        y = this.getY() + this.getHeight() / 2 - this.configureButton.getHeight() / 2;
        int rightX = x + this.getWidth();

        rightX -= this.removeButton.getWidth() + 2;
        this.removeButton.setPosition(rightX, y);

        rightX -= this.configureButton.getWidth() + 2;
        this.configureButton.setPosition(rightX, y);

        rightX -= this.toggleButton.getWidth() + 2;
        this.toggleButton.setPosition(rightX, y);

        this.nextWidgetX = this.toggleButton.getX() - 36;
        this.draggableRegionEndX = this.nextWidgetX - 1;
    }

    @Override
    public void postRenderHovered(ScreenContext ctx)
    {
        super.postRenderHovered(ctx);

        BaseConfigStatusIndicatorWidget<?> widget = this.data;
        int width = widget.getWidth();
        int height = widget.getHeight();
        float z = this.getZ();
        int x = ctx.mouseX + 10;
        int y = ctx.mouseY - 5;

        ShapeRenderUtils.renderRectangle(x - 2, y - 2, z + 10f, width + 4, height + 4, 0xC0000000);
        widget.renderAt(x, y, z + 15f, ctx);
    }
}
