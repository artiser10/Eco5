package doctor.eco5.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Gradient {
    public static Component gradient(String text, Integer color_from, Integer color_to) {
        List<HashMap<String, String>> data = pairs(text, color_from, color_to);
        Component component = Component.text("");
        for (HashMap<String, String> hashMap : data) {
            String color = "";
            String charr = "";
            for (String key : hashMap.keySet()) {
                color = key;
                charr = hashMap.get(key);
                break;
            }
            String[] spl = color.split(";");
            Component temp = Component.text(charr).color(TextColor.color(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2])));
            component = component.append(temp);
        }
        return component;
    }

    public static Component gradient(String text, Integer color_left, Integer color_middle, Integer color_right, Boolean space) {

        String first_part=text.substring(0, text.length() / 2);
        String second_part=text.substring(text.length() / 2);

        Component component_a = Gradient.gradient(first_part, color_left, color_middle);
        Component component_b = Gradient.gradient(second_part, color_middle, color_right);
        if (space) {
            return component_a.append(Component.text(" ")).append(component_b);
        } else {
            return component_a.append(component_b);
        }
    }



    public static List<HashMap<String, String>> pairs(String str, Integer color_a, Integer color_b) {
        List<HashMap<String, String>> list = new ArrayList<>();
        int ra = (color_a >> 16) & 255;
        int ga = (color_a >> 8) & 255;
        int ba = color_a & 255;
        int rb = (color_b >> 16) & 255;
        int gb = (color_b >> 8) & 255;
        int bb = color_b & 255;

        int l = str.length();

        int delta_r = ra-rb;
        int delta_g = ga-gb;
        int delta_b = ba-bb;
        float step_r = - 1F * delta_r / (l - 1);
        float step_g = - 1F * delta_g / (l - 1);
        float step_b = - 1F * delta_b / (l - 1);
        for (int i = 0; i < str.length(); i++) {
            int nr = (int) Math.abs(ra + (i * step_r));
            int ng = (int) Math.abs(ga + (i * step_g));
            int nb = (int) Math.abs(ba + (i * step_b));

            HashMap<String, String> hashMap = new HashMap<>();
            if (str.length() - i == 1) {
                hashMap.put(rb + ";" + gb + ";" + bb, String.valueOf(str.toCharArray()[i]));
                list.add(hashMap);
            } else {
                hashMap.put(nr + ";" + ng + ";" + nb, String.valueOf(str.toCharArray()[i]));
                list.add(hashMap);
            }
        }
        return list;
    }

    public static Component from_message(String msg) {
        Component component = Component.text("");
        String[] sspl = msg.split("<g>");
        List<String> list = new ArrayList<>();
        List<Integer> colors;
        for (String g : sspl) {
            String[] gspl = g.split("</g>");
            list.add(gspl[0]);
        }
        int i = 0;
        for (String l : list) {
            colors = new ArrayList<>();
            i = i + 1;
            System.out.println(i + ". " + l);
            if (l.startsWith("[")) {
                String colors_h_s = l.substring(1, 24);
                String[] colors_h = colors_h_s.split(";");
                for (String color_h : colors_h) {
                    String color_s = "0x" + color_h.substring(1);
                    colors.add(Integer.decode(color_s));
                }
                String text = l.substring(25);
                Component part = Gradient.gradient(text, colors.get(0), colors.get(1), colors.get(2), true);
                component = component.append(part);
            } else {
                component = component.append(Component.text(" " + l + " "));
            }
        }
        return component;
    }
}
