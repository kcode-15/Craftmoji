package dev.kcode15.craftmoji;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

/** A paginated picker for the Unicode emoji supported by the remote catalog. */
public final class EmojiPickerScreen extends Screen {
    private static final int COLUMNS = 8;
    private static final int ROWS = 5;
    private static final int PAGE_SIZE = COLUMNS * ROWS;
    private static final int BUTTON_WIDTH = 34;
    private static final int BUTTON_HEIGHT = 27;
    private static final int GAP = 3;

    private int page;
    private String displayedStatus;

    public EmojiPickerScreen() {
        super(Text.literal("Craftmoji"));
    }

    @Override
    protected void init() {
        rebuildWidgets();
    }

    @Override
    public void tick() {
        super.tick();
        String currentStatus = EmojiCatalog.getStatus();
        if (!Objects.equals(displayedStatus, currentStatus)) {
            rebuildWidgets();
        }
    }

    private void rebuildWidgets() {
        clearChildren();
        displayedStatus = EmojiCatalog.getStatus();

        if ("loading".equals(displayedStatus)) {
            return;
        }

        if ("not-loaded".equals(displayedStatus)) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Load emojis"), button ->
                            EmojiCatalog.load())
                    .dimensions(width / 2 - 50, height / 2 + 20, 100, 20)
                    .build());
            return;
        }

        if ("error".equals(displayedStatus)) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Try again"), button ->
                            EmojiCatalog.load())
                    .dimensions(width / 2 - 50, height / 2 + 20, 100, 20)
                    .build());
            return;
        }

        List<EmojiCatalog.Emoji> emojis = EmojiCatalog.getEmojis();
        int pageCount = Math.max(1, (emojis.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        page = Math.max(0, Math.min(page, pageCount - 1));

        int gridWidth = COLUMNS * BUTTON_WIDTH + (COLUMNS - 1) * GAP;
        int startX = (width - gridWidth) / 2;
        int startY = Math.max(45, (height - ROWS * (BUTTON_HEIGHT + GAP)) / 2 - 10);
        int firstIndex = page * PAGE_SIZE;
        int lastIndex = Math.min(firstIndex + PAGE_SIZE, emojis.size());

        for (int index = firstIndex; index < lastIndex; index++) {
            EmojiCatalog.Emoji emoji = emojis.get(index);
            int localIndex = index - firstIndex;
            int x = startX + (localIndex % COLUMNS) * (BUTTON_WIDTH + GAP);
            int y = startY + (localIndex / COLUMNS) * (BUTTON_HEIGHT + GAP);

            addDrawableChild(ButtonWidget.builder(Text.literal(emoji.glyph()), button ->
                            client.setScreen(new ChatScreen(emoji.glyph())))
                    .dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
                    .tooltip(net.minecraft.client.gui.tooltip.Tooltip.of(Text.literal(emoji.name())))
                    .build());
        }

        int controlsY = startY + ROWS * (BUTTON_HEIGHT + GAP) + 8;
        addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> {
                    page--;
                    rebuildWidgets();
                })
                .dimensions(width / 2 - 110, controlsY, 40, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
                .dimensions(width / 2 - 45, controlsY, 90, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> {
                    page++;
                    rebuildWidgets();
                })
                .dimensions(width / 2 + 70, controlsY, 40, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);

        if ("loading".equals(displayedStatus) || "not-loaded".equals(displayedStatus)) {
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.literal("Loading emojis…"), width / 2, height / 2 - 5, 0xFFFFFF);
        } else if ("error".equals(displayedStatus)) {
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.literal("Could not load emojis: " + EmojiCatalog.getError()),
                    width / 2, height / 2 - 5, 0xFF7777);
        } else {
            int pageCount = Math.max(1,
                    (EmojiCatalog.getEmojis().size() + PAGE_SIZE - 1) / PAGE_SIZE);
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.literal("Page " + (page + 1) + " / " + pageCount
                            + " — choose an emoji to add it to chat"),
                    width / 2, height - 23, 0xFFFFFF);
        }
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(null);
        }
    }
}
