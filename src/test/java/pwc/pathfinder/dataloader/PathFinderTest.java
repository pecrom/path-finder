package pwc.pathfinder.dataloader;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwc.pathfinder.dataloader.common.Country;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathFinderTest {

    private static final String VALID_MAP_DATA = "[\n" +
            "\t{\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"common\": \"Aruba\",\n" +
            "\t\t\t\"official\": \"Aruba\",\n" +
            "\t\t\t\"native\": {\n" +
            "\t\t\t\t\"nld\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\t\"pap\": {\"official\": \"Aruba\", \"common\": \"Aruba\"}\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"tld\": [\".aw\"],\n" +
            "\t\t\"cca2\": \"AW\",\n" +
            "\t\t\"ccn3\": \"533\",\n" +
            "\t\t\"cca3\": \"ABW\",\n" +
            "\t\t\"cioc\": \"ARU\",\n" +
            "\t\t\"independent\": false,\n" +
            "\t\t\"status\": \"officially-assigned\",\n" +
            "\t\t\"currency\": [\"AWG\"],\n" +
            "\t\t\"callingCode\": [\"297\"],\n" +
            "\t\t\"capital\": [\"Oranjestad\"],\n" +
            "\t\t\"altSpellings\": [\"AW\"],\n" +
            "\t\t\"region\": \"Americas\",\n" +
            "\t\t\"subregion\": \"Caribbean\",\n" +
            "\t\t\"languages\": {\"nld\": \"Dutch\", \"pap\": \"Papiamento\"},\n" +
            "\t\t\"translations\": {\n" +
            "\t\t\t\"ces\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"deu\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"fra\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"hrv\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"ita\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"jpn\": {\"official\": \"\\u30a2\\u30eb\\u30d0\", \"common\": \"\\u30a2\\u30eb\\u30d0\"},\n" +
            "\t\t\t\"nld\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"por\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"rus\": {\"official\": \"\\u0410\\u0440\\u0443\\u0431\\u0430\", \"common\": \"\\u0410\\u0440\\u0443\\u0431\\u0430\"},\n" +
            "\t\t\t\"slk\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"spa\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"fin\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"est\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"zho\": {\"official\": \"\\u963F\\u9C81\\u5DF4\", \"common\": \"\\u963F\\u9C81\\u5DF4\"},\n" +
            "\t\t\t\"pol\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"urd\": {\"official\": \"\\u0627\\u0631\\u0648\\u0628\\u0627\", \"common\": \"\\u0627\\u0631\\u0648\\u0628\\u0627\"},\n" +
            "\t\t\t\"kor\": {\"official\": \"\\uc544\\ub8e8\\ubc14\", \"common\": \"\\uc544\\ub8e8\\ubc14\"}\n" +
            "\n" +
            "\t\t},\n" +
            "\t\t\"latlng\": [12.5, -69.96666666],\n" +
            "\t\t\"demonym\": \"Aruban\",\n" +
            "\t\t\"landlocked\": false,\n" +
            "\t\t\"borders\": [\"AFG\"],\n" +
            "\t\t\"area\": 180,\n" +
            "\t\t\"flag\": \"\\ud83c\\udde6\\ud83c\\uddfc\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"common\": \"Afghanistan\",\n" +
            "\t\t\t\"official\": \"Islamic Republic of Afghanistan\",\n" +
            "\t\t\t\"native\": {\n" +
            "\t\t\t\t\"prs\": {\n" +
            "\t\t\t\t\t\"official\": \"\\u062c\\u0645\\u0647\\u0648\\u0631\\u06cc \\u0627\\u0633\\u0644\\u0627\\u0645\\u06cc \\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646\",\n" +
            "\t\t\t\t\t\"common\": \"\\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"pus\": {\n" +
            "\t\t\t\t\t\"official\": \"\\u062f \\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646 \\u0627\\u0633\\u0644\\u0627\\u0645\\u064a \\u062c\\u0645\\u0647\\u0648\\u0631\\u06cc\\u062a\",\n" +
            "\t\t\t\t\t\"common\": \"\\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"tuk\": {\n" +
            "\t\t\t\t\t\"official\": \"Owganystan Yslam Respublikasy\",\n" +
            "\t\t\t\t\t\"common\": \"Owganystan\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"tld\": [\".af\"],\n" +
            "\t\t\"cca2\": \"AF\",\n" +
            "\t\t\"ccn3\": \"004\",\n" +
            "\t\t\"cca3\": \"AFG\",\n" +
            "\t\t\"cioc\": \"AFG\",\n" +
            "\t\t\"independent\": true,\n" +
            "\t\t\"status\": \"officially-assigned\",\n" +
            "\t\t\"currency\": [\"AFN\"],\n" +
            "\t\t\"callingCode\": [\"93\"],\n" +
            "\t\t\"capital\": [\"Kabul\"],\n" +
            "\t\t\"altSpellings\": [\"AF\", \"Af\\u0121\\u0101nist\\u0101n\"],\n" +
            "\t\t\"region\": \"Asia\",\n" +
            "\t\t\"subregion\": \"Southern Asia\",\n" +
            "\t\t\"languages\": {\"prs\": \"Dari\", \"pus\": \"Pashto\", \"tuk\": \"Turkmen\"},\n" +
            "\t\t\"translations\": {\n" +
            "\t\t\t\"ces\": {\"official\": \"Afgh\\u00e1nsk\\u00e1 isl\\u00e1msk\\u00e1 republika\", \"common\": \"Afgh\\u00e1nist\\u00e1n\"},\n" +
            "\t\t\t\"cym\": {\"official\": \"Gweriniaeth Islamaidd Affganistan\", \"common\": \"Affganistan\"},\n" +
            "\t\t\t\"deu\": {\"official\": \"Islamische Republik Afghanistan\", \"common\": \"Afghanistan\"},\n" +
            "\t\t\t\"fra\": {\"official\": \"R\\u00e9publique islamique d'Afghanistan\", \"common\": \"Afghanistan\"},\n" +
            "\t\t\t\"hrv\": {\"official\": \"Islamska Republika Afganistan\", \"common\": \"Afganistan\"},\n" +
            "\t\t\t\"ita\": {\"official\": \"Repubblica islamica dell'Afghanistan\", \"common\": \"Afghanistan\"},\n" +
            "\t\t\t\"jpn\": {\"official\": \"\\u30a2\\u30d5\\u30ac\\u30cb\\u30b9\\u30bf\\u30f3\\u00b7\\u30a4\\u30b9\\u30e9\\u30e0\\u5171\\u548c\\u56fd\", \"common\": \"\\u30a2\\u30d5\\u30ac\\u30cb\\u30b9\\u30bf\\u30f3\"},\n" +
            "\t\t\t\"nld\": {\"official\": \"Islamitische Republiek Afghanistan\", \"common\": \"Afghanistan\"},\n" +
            "\t\t\t\"por\": {\"official\": \"Rep\\u00fablica Isl\\u00e2mica do Afeganist\\u00e3o\", \"common\": \"Afeganist\\u00e3o\"},\n" +
            "\t\t\t\"rus\": {\"official\": \"\\u0418\\u0441\\u043b\\u0430\\u043c\\u0441\\u043a\\u0430\\u044f \\u0420\\u0435\\u0441\\u043f\\u0443\\u0431\\u043b\\u0438\\u043a\\u0430 \\u0410\\u0444\\u0433\\u0430\\u043d\\u0438\\u0441\\u0442\\u0430\\u043d\", \"common\": \"\\u0410\\u0444\\u0433\\u0430\\u043d\\u0438\\u0441\\u0442\\u0430\\u043d\"},\n" +
            "\t\t\t\"slk\": {\"official\": \"Afg\\u00e1nsky islamsk\\u00fd \\u0161t\\u00e1t\", \"common\": \"Afganistan\"},\n" +
            "\t\t\t\"spa\": {\"official\": \"Rep\\u00fablica Isl\\u00e1mica de Afganist\\u00e1n\", \"common\": \"Afganist\\u00e1n\"},\n" +
            "\t\t\t\"fin\": {\"official\": \"Afganistanin islamilainen tasavalta\", \"common\": \"Afganistan\"},\n" +
            "\t\t\t\"est\": {\"official\": \"Afganistani Islamivabariik\", \"common\": \"Afganistan\"},\n" +
            "\t\t\t\"zho\": {\"official\": \"\\u963F\\u5BCC\\u6C57\\u4F0A\\u65AF\\u5170\\u5171\\u548C\\u56FD\", \"common\": \"\\u963F\\u5BCC\\u6C57\"},\n" +
            "\t\t\t\"pol\": {\"official\": \"Islamska Republika Afganistanu\", \"common\": \"Afganistan\"},\n" +
            "\t\t\t\"urd\": {\"official\": \"\\u0627\\u0633\\u0644\\u0627\\u0645\\u06cc \\u062c\\u0645\\u06c1\\u0648\\u0631\\u06cc\\u06c1 \\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646\", \"common\": \"\\u0627\\u0641\\u063a\\u0627\\u0646\\u0633\\u062a\\u0627\\u0646\"},\n" +
            "\t\t\t\"kor\": {\"official\": \"\\uc544\\ud504\\uac00\\ub2c8\\uc2a4\\ud0c4 \\uc774\\uc2ac\\ub78c \\uacf5\\ud654\\uad6d\", \"common\": \"\\uc544\\ud504\\uac00\\ub2c8\\uc2a4\\ud0c4\"}\n" +
            "\n" +
            "\t\t},\n" +
            "\t\t\"latlng\": [33, 65],\n" +
            "\t\t\"demonym\": \"Afghan\",\n" +
            "\t\t\"landlocked\": true,\n" +
            "\t\t\"borders\": [\"AGO\", \"ABW\"],\n" +
            "\t\t\"area\": 652230,\n" +
            "\t\t\"flag\": \"\\ud83c\\udde6\\ud83c\\uddeb\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"common\": \"Angola\",\n" +
            "\t\t\t\"official\": \"Republic of Angola\",\n" +
            "\t\t\t\"native\": {\n" +
            "\t\t\t\t\"por\": {\"official\": \"Rep\\u00fablica de Angola\", \"common\": \"Angola\"}\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"tld\": [\".ao\"],\n" +
            "\t\t\"cca2\": \"AO\",\n" +
            "\t\t\"ccn3\": \"024\",\n" +
            "\t\t\"cca3\": \"AGO\",\n" +
            "\t\t\"cioc\": \"ANG\",\n" +
            "\t\t\"independent\": true,\n" +
            "\t\t\"status\": \"officially-assigned\",\n" +
            "\t\t\"currency\": [\"AOA\"],\n" +
            "\t\t\"callingCode\": [\"244\"],\n" +
            "\t\t\"capital\": [\"Luanda\"],\n" +
            "\t\t\"altSpellings\": [\n" +
            "\t\t\t\"AO\",\n" +
            "\t\t\t\"Rep\\u00fablica de Angola\",\n" +
            "\t\t\t\"\\u0281\\u025bpublika de an'\\u0261\\u0254la\"\n" +
            "\t\t],\n" +
            "\t\t\"region\": \"Africa\",\n" +
            "\t\t\"subregion\": \"Middle Africa\",\n" +
            "\t\t\"languages\": {\"por\": \"Portuguese\"},\n" +
            "\t\t\"translations\": {\n" +
            "\t\t\t\"ces\": {\"official\": \"Angolsk\\u00e1 republika\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"cym\": {\"official\": \"Gweriniaeth Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"deu\": {\"official\": \"Republik Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"fra\": {\"official\": \"R\\u00e9publique d'Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"hrv\": {\"official\": \"Republika Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"ita\": {\"official\": \"Repubblica dell'Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"jpn\": {\"official\": \"\\u30a2\\u30f3\\u30b4\\u30e9\\u5171\\u548c\\u56fd\", \"common\": \"\\u30a2\\u30f3\\u30b4\\u30e9\"},\n" +
            "\t\t\t\"nld\": {\"official\": \"Republiek Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"por\": {\"official\": \"Rep\\u00fablica de Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"rus\": {\"official\": \"\\u0420\\u0435\\u0441\\u043f\\u0443\\u0431\\u043b\\u0438\\u043a\\u0430 \\u0410\\u043d\\u0433\\u043e\\u043b\\u0430\", \"common\": \"\\u0410\\u043d\\u0433\\u043e\\u043b\\u0430\"},\n" +
            "\t\t\t\"slk\": {\"official\": \"Angolsk\\u00e1 republika\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"spa\": {\"official\": \"Rep\\u00fablica de Angola\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"fin\": {\"official\": \"Angolan tasavalta\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"est\": {\"official\": \"Angola Vabariik\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"zho\": {\"official\": \"\\u5B89\\u54E5\\u62C9\\u5171\\u548C\\u56FD\", \"common\": \"\\u5B89\\u54E5\\u62C9\"},\n" +
            "\t\t\t\"pol\": {\"official\": \"Republika Angoli\", \"common\": \"Angola\"},\n" +
            "\t\t\t\"urd\": {\"official\": \"\\u062c\\u0645\\u06c1\\u0648\\u0631\\u06cc\\u06c1 \\u0627\\u0646\\u06af\\u0648\\u0644\\u06c1\", \"common\": \"\\u0627\\u0646\\u06af\\u0648\\u0644\\u06c1\"},\n" +
            "\t\t\t\"kor\": {\"official\": \"\\uc559\\uace8\\ub77c \\uacf5\\ud654\\uad6d\", \"common\": \"\\uc559\\uace8\\ub77c\"}\n" +
            "\n" +
            "\t\t},\n" +
            "\t\t\"latlng\": [-12.5, 18.5],\n" +
            "\t\t\"demonym\": \"Angolan\",\n" +
            "\t\t\"landlocked\": false,\n" +
            "\t\t\"borders\": [\"AFG\"],\n" +
            "\t\t\"area\": 1246700,\n" +
            "\t\t\"flag\": \"\\ud83c\\udde6\\ud83c\\uddf4\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"common\": \"Anguilla\",\n" +
            "\t\t\t\"official\": \"Anguilla\",\n" +
            "\t\t\t\"native\": {\"eng\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"} }\n" +
            "\t\t},\n" +
            "\t\t\"tld\": [\".ai\"],\n" +
            "\t\t\"cca2\": \"AI\",\n" +
            "\t\t\"ccn3\": \"660\",\n" +
            "\t\t\"cca3\": \"AIA\",\n" +
            "\t\t\"cioc\": \"\",\n" +
            "\t\t\"independent\": false,\n" +
            "\t\t\"status\": \"officially-assigned\",\n" +
            "\t\t\"currency\": [\"XCD\"],\n" +
            "\t\t\"callingCode\": [\"1264\"],\n" +
            "\t\t\"capital\": [\"The Valley\"],\n" +
            "\t\t\"altSpellings\": [\"AI\"],\n" +
            "\t\t\"region\": \"Americas\",\n" +
            "\t\t\"subregion\": \"Caribbean\",\n" +
            "\t\t\"languages\": {\"eng\": \"English\"},\n" +
            "\t\t\"translations\": {\n" +
            "\t\t\t\"ces\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"deu\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"fra\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"hrv\": {\"official\": \"Anguilla\", \"common\": \"Angvila\"},\n" +
            "\t\t\t\"ita\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"jpn\": {\"official\": \"\\u30a2\\u30f3\\u30b0\\u30a3\\u30e9\", \"common\": \"\\u30a2\\u30f3\\u30ae\\u30e9\"},\n" +
            "\t\t\t\"nld\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"por\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"rus\": {\"official\": \"\\u0410\\u043d\\u0433\\u0438\\u043b\\u044c\\u044f\", \"common\": \"\\u0410\\u043d\\u0433\\u0438\\u043b\\u044c\\u044f\"},\n" +
            "\t\t\t\"slk\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"spa\": {\"official\": \"Anguila\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"fin\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"est\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"zho\": {\"official\": \"\\u5B89\\u572D\\u62C9\", \"common\": \"\\u5B89\\u572D\\u62C9\"},\n" +
            "\t\t\t\"pol\": {\"official\": \"Anguilla\", \"common\": \"Anguilla\"},\n" +
            "\t\t\t\"urd\": {\"official\": \"\\u0627\\u06cc\\u0646\\u06af\\u0648\\u06cc\\u0644\\u0627\", \"common\": \"\\u0627\\u06cc\\u0646\\u06af\\u0648\\u06cc\\u0644\\u0627\"},\n" +
            "\t\t\t\"kor\": {\"official\": \"\\uc575\\uadc8\\ub77c\", \"common\": \"\\uc575\\uadc8\\ub77c\"}\n" +
            "\n" +
            "\t\t},\n" +
            "\t\t\"latlng\": [18.25, -63.16666666],\n" +
            "\t\t\"demonym\": \"Anguillian\",\n" +
            "\t\t\"landlocked\": false,\n" +
            "\t\t\"borders\": [],\n" +
            "\t\t\"area\": 91,\n" +
            "\t\t\"flag\": \"\\ud83c\\udde6\\ud83c\\uddee\"\n" +
            "\t}\n" +
            "]";

    private static final String INVALID_MAP_DATA = "[\n" +
            "\t{\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"common\": \"Aruba\",\n" +
            "\t\t\t\"official\": \"Aruba\",\n" +
            "\t\t\t\"native\": {\n" +
            "\t\t\t\t\"nld\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\t\"pap\": {\"official\": \"Aruba\", \"common\": \"Aruba\"}\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"tld\": [\".aw\"],\n" +
            "\t\t\"cca2\": \"AW\",\n" +
            "\t\t\"ccn3\": \"533\",\n" +
            "\t\t\"cioc\": \"ARU\",\n" +
            "\t\t\"independent\": false,\n" +
            "\t\t\"status\": \"officially-assigned\",\n" +
            "\t\t\"currency\": [\"AWG\"],\n" +
            "\t\t\"callingCode\": [\"297\"],\n" +
            "\t\t\"capital\": [\"Oranjestad\"],\n" +
            "\t\t\"altSpellings\": [\"AW\"],\n" +
            "\t\t\"region\": \"Americas\",\n" +
            "\t\t\"subregion\": \"Caribbean\",\n" +
            "\t\t\"languages\": {\"nld\": \"Dutch\", \"pap\": \"Papiamento\"},\n" +
            "\t\t\"translations\": {\n" +
            "\t\t\t\"ces\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"deu\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"fra\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"hrv\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"ita\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"jpn\": {\"official\": \"\\u30a2\\u30eb\\u30d0\", \"common\": \"\\u30a2\\u30eb\\u30d0\"},\n" +
            "\t\t\t\"nld\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"por\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"rus\": {\"official\": \"\\u0410\\u0440\\u0443\\u0431\\u0430\", \"common\": \"\\u0410\\u0440\\u0443\\u0431\\u0430\"},\n" +
            "\t\t\t\"slk\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"spa\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"fin\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"est\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"zho\": {\"official\": \"\\u963F\\u9C81\\u5DF4\", \"common\": \"\\u963F\\u9C81\\u5DF4\"},\n" +
            "\t\t\t\"pol\": {\"official\": \"Aruba\", \"common\": \"Aruba\"},\n" +
            "\t\t\t\"urd\": {\"official\": \"\\u0627\\u0631\\u0648\\u0628\\u0627\", \"common\": \"\\u0627\\u0631\\u0648\\u0628\\u0627\"},\n" +
            "\t\t\t\"kor\": {\"official\": \"\\uc544\\ub8e8\\ubc14\", \"common\": \"\\uc544\\ub8e8\\ubc14\"}\n" +
            "\n" +
            "\t\t},\n" +
            "\t\t\"latlng\": [12.5, -69.96666666],\n" +
            "\t\t\"demonym\": \"Aruban\",\n" +
            "\t\t\"landlocked\": false,\n" +
            "\t\t\"borders\": [],\n" +
            "\t\t\"area\": 180,\n" +
            "\t\t\"flag\": \"\\ud83c\\udde6\\ud83c\\uddfc\"\n" +
            "\t}\n" +
            "]";

    private static final String NON_EXISTING_COUNTRY_CODE = "XXX";

    private static final String EXISTING_COUNTRY_CODE = "ABW";

    @Test
    @DisplayName("Init without exception")
    public void initWithoutException() throws IOException {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(VALID_MAP_DATA));
        pathFinder.initPaths();
    }

    @Test
    @DisplayName("Init with exception")
    public void initWithException() throws IOException {
        InputStream mockedCountriesDataSteam = mock(InputStream.class);

        when(mockedCountriesDataSteam.read()).thenThrow(IOException.class);
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(mockedCountriesDataSteam);
        assertThrows(IOException.class, () -> pathFinder.initPaths());
    }

    @Test
    @DisplayName("Init with invalid data")
    public void initWithInvalidData() {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(INVALID_MAP_DATA));

        assertThrows(JsonMappingException.class, () -> pathFinder.initPaths());
    }

    @Test
    @DisplayName("Check if existing country exists")
    public void checkExistingCountry() throws IOException {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(VALID_MAP_DATA));
        pathFinder.initPaths();

        assertFalse(pathFinder.isNotCountryCodeExists(EXISTING_COUNTRY_CODE));
    }

    @Test
    @DisplayName("Check if country does not exist")
    public void checkNonExistingCountry() throws IOException {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(VALID_MAP_DATA));
        pathFinder.initPaths();

        assertTrue(pathFinder.isNotCountryCodeExists(NON_EXISTING_COUNTRY_CODE));
    }

    @Test
    @DisplayName("Finding shortest path")
    public void findValidShortestPath() throws IOException {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(VALID_MAP_DATA));
        pathFinder.initPaths();

        Set<Country> shortestPath = pathFinder.findShortestRoute("ABW","AGO");

        Country[] expected = new Country[]{new Country("ABW"), new Country("AFG"), new Country("AGO")};

        assertArrayEquals(expected, shortestPath.toArray());
    }

    @Test
    @DisplayName("Finding no path")
    public void findNoPath() throws IOException {
        PathFinder pathFinder = new PathFinder();

        pathFinder.setCountriesDataStream(createInputStream(VALID_MAP_DATA));
        pathFinder.initPaths();

        Set<Country> shortestPath = pathFinder.findShortestRoute("ABW","AIA");

        Set<Country> expected = Collections.emptySet();
        assertEquals(expected, shortestPath);

        assertArrayEquals(expected.toArray(), shortestPath.toArray());
    }

    private InputStream createInputStream(String data) {
        return new ByteArrayInputStream(data.getBytes());
    }
}
