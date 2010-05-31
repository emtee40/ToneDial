/*
 * Copyright (c) Oliver Bye 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.xpdeveloper.dialer.api5;

import net.xpdeveloper.dialer.ToneDialModel;
import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * I implement the ToneDial model using the API4 ToneGenerator API API4 does not
 * have a duration argument in the startTone method. Leaving the timing up to
 * the caller. Since ToneGeneration is on another thread the actual duration is
 * far from predictable. 
 * 
 * @author byeo
 * 
 */
public class ToneDialModelAPI5 extends ToneDialModel {
	private ToneGenerator _toneGenerator;

	/**
	 * Will throw @see java.lang.VerifyError if we try to call this on an older phone.
	 * @param volume
	 */
	public ToneDialModelAPI5() {
		this(80);
	}
	
	public ToneDialModelAPI5(int volume) {
		// AudioManager.STREAM_DTMF on API 7
		// http://developer.android.com/reference/android/media/ToneGenerator.html#ToneGenerator(int,
		// int)
		_toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, volume);
	}

	@Override
	protected synchronized void dialDigit(final int dtmfCode, final int pause)
			throws InterruptedException {

		// wait before tone as this helps a sleeping amp wake up
		// last pause isn't needed
		wait(TONE_DURATION + pause);

		_toneGenerator.startTone(dtmfCode, TONE_DURATION);
	}

	@Override
	public void release() {
		_toneGenerator.release();
	}

}