package converter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Main CLIENT = new Main();
    private static final Pattern xmlTag = Pattern.compile("<([^>]*)>([^<]*)</[^>]*>");
    private static final Pattern xmlTagSingle = Pattern.compile("<([^/]*)/>");
    private static final Pattern jsonNull = Pattern.compile("\\{ *\"([^\"]*)\" *: *null *\\}");
    private static final Pattern json = Pattern.compile("\\{ *\"([^\"]*)\" *: *\"([^\"]*) *\"\\}");

    public static void main(String[] args) {
        final String inputStr = CLIENT.getUserInput();
        CLIENT.convertJsonXml(inputStr);
    }

    private String getUserInput() {
        try (final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            return scanner.nextLine();
        }
    }

    private void convertJsonXml(String inputStr) {
        switch (inputStr.charAt(0)) {
            case '{':
                final Matcher matchJsonNull = jsonNull.matcher(inputStr);
                final Matcher matchJson = json.matcher(inputStr);
                if (matchJsonNull.find()) {
                    System.out.printf("<%s/>", matchJsonNull.group(1));
                } else if (matchJson.find()) {
                    System.out.printf("<%s>%s</%s>", matchJson.group(1), matchJson.group(2), matchJson.group(1));
                }
                break;
            case '<':
                final Matcher matchXmlTag = xmlTag.matcher(inputStr);
                final Matcher matchXmlTagSingle = xmlTagSingle.matcher(inputStr);
                if (matchXmlTag.find()) {
                    System.out.printf("{\"%s\" : \"%s\"}", matchXmlTag.group(1), matchXmlTag.group(2));
                } else if (matchXmlTagSingle.find()) {
                    System.out.printf("{\"%s\" : null }", matchXmlTagSingle.group(1));
                }
                break;
            default:
                System.out.println("Unsupported format given!");
        }
    }
}
