/*
 * Copyright 2012 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * The following is a list of character encodings for accented Turkish characters
 * ı => &#305; => &#x131;
 * ş => &#351; => &#x15F;
 * ç => &#231; => &ccedil;
 * ö => &#246; => &ouml;
 * ü => &#252; => &uuml;
 * ğ => &#287; => &#x11F;
 * İ => &#304; => &#x130;
 * Ş => &#350; => &#x15E;
 * Ç => &#199; => &Ccedil;
 * Ö => &#214; => &Ouml;
 * Ğ => &#286; => &#x11E;
 * Ü => &#220; => &Uuml;
 */

package org.ocpsoft.pretty.time.i18n;

import java.util.ListResourceBundle;

public class Resources_tr extends ListResourceBundle
{
    private static final Object[][] OBJECTS = new Object[][] {
                                                              { "CenturyPattern", "%n %u" },
                                                              { "CenturyFuturePrefix", "" },
                                                              { "CenturyFutureSuffix", " sonra" },
                                                              { "CenturyPastPrefix", "" },
                                                              { "CenturyPastSuffix", " önce" },
                                                              { "CenturyName", "yüzyıl" },
                                                              { "CenturyPluralName", "yüzyıl" },
                                                              { "DayPattern", "%n %u" },
                                                              { "DayFuturePrefix", "" },
                                                              { "DayFutureSuffix", " sonra" },
                                                              { "DayPastPrefix", "" },
                                                              { "DayPastSuffix", " önce" },
                                                              { "DayName", "gün" },
                                                              { "DayPluralName", "gün" },
                                                              { "DecadePattern", "%n %u" },
                                                              { "DecadeFuturePrefix", "" },
                                                              { "DecadeFutureSuffix", " sonra" },
                                                              { "DecadePastPrefix", "" },
                                                              { "DecadePastSuffix", " önce" },
                                                              { "DecadeName", "onyıl" },
                                                              { "DecadePluralName", "onyıl" },
                                                              { "HourPattern", "%n %u" },
                                                              { "HourFuturePrefix", "" },
                                                              { "HourFutureSuffix", " sonra" },
                                                              { "HourPastPrefix", "" },
                                                              { "HourPastSuffix", " önce" },
                                                              { "HourName", "saat" },
                                                              { "HourPluralName", "saat" },
                                                              { "JustNowPattern", "%u" },
                                                              { "JustNowFuturePrefix", "" },
                                                              { "JustNowFutureSuffix", "az sonra" },
                                                              { "JustNowPastPrefix", "az önce" },
                                                              { "JustNowPastSuffix", "" },
                                                              { "JustNowName", "" },
                                                              { "JustNowPluralName", "" },
                                                              { "MillenniumPattern", "%n %u" },
                                                              { "MillenniumFuturePrefix", "" },
                                                              { "MillenniumFutureSuffix", " sonra" },
                                                              { "MillenniumPastPrefix", "" },
                                                              { "MillenniumPastSuffix", " önce" },
                                                              { "MillenniumName", "milenyum" },
                                                              { "MillenniumPluralName", "milenyum" },
                                                              { "MillisecondPattern", "%n %u" },
                                                              { "MillisecondFuturePrefix", "" },
                                                              { "MillisecondFutureSuffix", " sonra" },
                                                              { "MillisecondPastPrefix", "" },
                                                              { "MillisecondPastSuffix", " önce" },
                                                              { "MillisecondName", "salise" },
                                                              { "MillisecondPluralName", "salise" },
                                                              { "MinutePattern", "%n %u" },
                                                              { "MinuteFuturePrefix", "" },
                                                              { "MinuteFutureSuffix", " sonra" },
                                                              { "MinutePastPrefix", "" },
                                                              { "MinutePastSuffix", " önce" },
                                                              { "MinuteName", "dakika" },
                                                              { "MinutePluralName", "dakika" },
                                                              { "MonthPattern", "%n %u" },
                                                              { "MonthFuturePrefix", "" },
                                                              { "MonthFutureSuffix", " sonra" },
                                                              { "MonthPastPrefix", "" },
                                                              { "MonthPastSuffix", " önce" },
                                                              { "MonthName", "ay" },
                                                              { "MonthPluralName", "ay" },
                                                              { "SecondPattern", "%n %u" },
                                                              { "SecondFuturePrefix", "" },
                                                              { "SecondFutureSuffix", " sonra" },
                                                              { "SecondPastPrefix", "" },
                                                              { "SecondPastSuffix", " önce" },
                                                              { "SecondName", "saniye" },
                                                              { "SecondPluralName", "saniye" },
                                                              { "WeekPattern", "%n %u" },
                                                              { "WeekFuturePrefix", "" },
                                                              { "WeekFutureSuffix", " sonra" },
                                                              { "WeekPastPrefix", "" },
                                                              { "WeekPastSuffix", " önce" },
                                                              { "WeekName", "hafta" },
                                                              { "WeekPluralName", "hafta" },
                                                              { "YearPattern", "%n %u" },
                                                              { "YearFuturePrefix", "" },
                                                              { "YearFutureSuffix", " sonra" },
                                                              { "YearPastPrefix", "" },
                                                              { "YearPastSuffix", " önce" },
                                                              { "YearName", "yıl" },
                                                              { "YearPluralName", "yıl" },
                                                              { "AbstractTimeUnitPattern", "" },
                                                              { "AbstractTimeUnitFuturePrefix", "" },
                                                              { "AbstractTimeUnitFutureSuffix", "" },
                                                              { "AbstractTimeUnitPastPrefix", "" },
                                                              { "AbstractTimeUnitPastSuffix", "" },
                                                              { "AbstractTimeUnitName", "" },
                                                              { "AbstractTimeUnitPluralName", "" } };

    @Override
    public Object[][] getContents()
    {
        return OBJECTS;
    }

}
