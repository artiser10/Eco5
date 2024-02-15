package doctor.eco5;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

public class Staff {
    public static Component badge = Component.text("[").color(TextColor.color(255, 183, 228)).append(Component.text("Ⓢ").color(TextColor.color(140, 126, 255)).hoverEvent(HoverEvent.showText(Component.text("✔").color(TextColor.color(10,240,10)).append(Component.text(" Является сотрудником."))))).append(Component.text("] ").color(TextColor.color(255, 183, 228)));
}