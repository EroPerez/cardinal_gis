package cu.phibrain.plugins.cardinal.io.network.api.adapter;

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cu.phibrain.plugins.cardinal.io.utils.JodaTimeHelper;

import java.text.ParseException;
import java.util.Date;


/**
 * ISO 8601 date parsing utility.  Designed for parsing the ISO subset used in
 * Dublin Core, RSS 1.0, and Atom.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: ISO8601DateParser.java,v 1.2 2005/06/03 20:25:29 snoopdave Exp $
 */
public class ISO8601DateParser {

    /**
     * 2004-06-14T19:GMT20:30Z
     * 2004-06-20T06:GMT22:01Z
     * <p>
     * http://www.cl.cam.ac.uk/~mgk25/iso-time.html
     * <p>
     * http://www.intertwingly.net/wiki/pie/DateTime
     * <p>
     * http://www.w3.org/TR/NOTE-datetime
     * <p>
     * Different standards may need different levels of granularity in the date and
     * time, so this profile_details defines six levels. Standards that reference this
     * profile_details should specify one or more of these granularities. If a given
     * standard allows more than one granularity, it should specify the meaning of
     * the dates and times with reduced precision, for example, the result of
     * comparing two dates with different precisions.
     * <p>
     * The formats are as follows. Exactly the components shown here must be
     * present, with exactly this punctuation. Note that the "T" appears literally
     * in the string, to indicate the beginning of the time element, as specified in
     * ISO 8601.
     * <p>
     * Year:
     * YYYY (eg 1997)
     * Year and month:
     * YYYY-MM (eg 1997-07)
     * Complete date:
     * YYYY-MM-DD (eg 1997-07-16)
     * Complete date plus hours and minutes:
     * YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
     * Complete date plus hours, minutes and seconds:
     * YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
     * Complete date plus hours, minutes, seconds and a decimal fraction of a
     * second
     * YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
     * <p>
     * where:
     * <p>
     * YYYY = four-digit year
     * MM   = two-digit month (01=January, etc.)
     * DD   = two-digit day of month (01 through 31)
     * hh   = two digits of hour (00 through 23) (am/pm NOT allowed)
     * mm   = two digits of minute (00 through 59)
     * ss   = two digits of second (00 through 59)
     * s    = one or more digits representing a decimal fraction of a second
     * TZD  = time zone designator (Z or +hh:mm or -hh:mm)
     */
    public static Date parse(String input) throws java.text.ParseException {

        if (input == null) throw new ParseException("Null", 0);

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        return JodaTimeHelper.parseStringAsDate(input);
    }

    public static String format(Date date, String format) {
        return JodaTimeHelper.formatDate(format, date);
    }
}
