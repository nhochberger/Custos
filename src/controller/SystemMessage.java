package controller;

import hochberger.utilities.eventbus.Event;
import hochberger.utilities.text.CommonDateTimeFormatters;
import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.I18N;

import java.awt.Color;

import org.joda.time.DateTime;

import view.ColorProvider;

public class SystemMessage implements Event {

    public static enum MessageSeverity {

        NORMAL {
            @Override
            public Color getColorFrom(final ColorProvider colorProvider) {
                return colorProvider.foregroundColor();
            }
        },
        WARNING {
            @Override
            public Color getColorFrom(final ColorProvider colorProvider) {
                return colorProvider.highlightColor();
            }
        },
        SEVERE {
            @Override
            public Color getColorFrom(final ColorProvider colorProvider) {
                return colorProvider.warningColor();
            }
        },
        SUCCESS {
            @Override
            public Color getColorFrom(final ColorProvider colorProvider) {
                return colorProvider.everythingOkColor();
            }
        };

        public abstract Color getColorFrom(ColorProvider colorProvider);
    }

    private final MessageSeverity severity;
    private final String text;
    private final DateTime time;

    public SystemMessage(final MessageSeverity severity, final I18N text) {
        this(severity, text.toString());
    }

    public SystemMessage(final MessageSeverity severity, final String text) {
        super();
        this.severity = severity;
        this.text = text;
        this.time = DateTime.now();
    }

    public MessageSeverity getSeverity() {
        return this.severity;
    }

    public String getText() {
        return this.text;
    };

    public DateTime getTime() {
        return this.time;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CommonDateTimeFormatters.dayMonthYearOnlyDigits().print(this.time));
        result.append(Text.space());
        result.append(CommonDateTimeFormatters.hourMinuteSecond().print(this.time));
        result.append(": ");
        result.append(this.text);
        return result.toString();
    }
}
