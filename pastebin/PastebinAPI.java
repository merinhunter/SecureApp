package pastebin;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;

import cipher.EncFile;

import java.util.ArrayList;

public class PastebinAPI {
	private final String devKey = "";
	private PastebinFactory factory;
	private Pastebin pastebin;

	public PastebinAPI() {
		factory = new PastebinFactory();
		pastebin = factory.createPastebin(devKey);
	}

	public ArrayList<String> post(ArrayList<EncFile> files) {
		ArrayList<String> urls = new ArrayList<>();

		for (EncFile file : files) {
			System.out.println(file.toBase64());

			PasteBuilder pastebuilder = factory.createPaste();
			pastebuilder.setTitle("");
			pastebuilder.setRaw(file.toBase64());
			pastebuilder.setMachineFriendlyLanguage("text");
			pastebuilder.setVisiblity(PasteVisiblity.Unlisted);
			pastebuilder.setExpire(PasteExpire.OneDay);

			Paste paste = pastebuilder.build();
			Response<String> postResult = pastebin.post(paste);
			if (postResult.hasError()) {
				System.out.println("ERROR: " + postResult.getError());
				return null;
			}

			System.out.print("Paste published! Url: " + postResult.get());
			urls.add(postResult.get());
		}

		return urls;
	}
}
