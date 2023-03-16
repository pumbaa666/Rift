package ch.correvon.rift.antiDeco.tools;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.swing.text.JTextComponent;
import ch.correvon.rift.antiDeco.tools.MacroSequenceElement.MacroSequenceElementType;

public class KeyboardMacro extends Thread
{
	public KeyboardMacro(MacroSequence sequence, int sleepBetweenTwoSequences, int sleepBetweenTwoSequencesRandom, int sleepBetweenTwoKeysStart, int sleepBetweenTwoKeysEnd, boolean checkShuffleSequence)
	{
		try
		{
			this.robot = new Robot();
			log("Robot créé");
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
		this.sequence = sequence;
		this.sleepBetweenTwoSequences = sleepBetweenTwoSequences;
		this.sleepBetweenTwoSequencesRandom = sleepBetweenTwoSequencesRandom;
		this.sleepBetweenTwoKeysStart = sleepBetweenTwoKeysStart;
		this.sleepBetweenTwoKeysEnd = sleepBetweenTwoKeysEnd;
		this.checkShuffleSequence = checkShuffleSequence;
		this.exit = false;
	}

	public void addTextLogger(JTextComponent component)
	{
		this.logger = component;
	}

	public void exit()
	{
		this.exit = true;
	}

	@Override
	public void run()
	{
		log("début de la macro");
		try
		{
			while(!this.exit)
			{
				int rnd = random.nextInt(this.sleepBetweenTwoSequencesRandom * 2);
				int sleepBetweenTwoSequences = this.sleepBetweenTwoSequences + this.sleepBetweenTwoSequencesRandom - rnd;

				logLN("\n-----------------------");
				logLN("Prochaine séquence " + sleepBetweenTwoSequences + " ms [" + this.getFuturTime(sleepBetweenTwoSequences) + "]");
				logLN("-----------------------");
				if(sleepBetweenTwoSequences < 0)
					sleepBetweenTwoSequences = 0;
				super.sleep(sleepBetweenTwoSequences);

				List<MacroSequenceElement> sequence;
				if(this.checkShuffleSequence)
					sequence = this.sequence.getRandomizedSequence();
				else
					sequence = this.sequence.getSequence();
				for(MacroSequenceElement element:sequence)
				{
					if(this.exit)
						break;

					MacroSequenceElementType type = element.getType();
					switch(type)
					{
						case KEYBOARD: this.pressKey(element); break;
						case MOUSE: this.pressKey(element); break;
						case STRING: 
							List<MacroSequenceElement> suite = element.getSuite();
							for(MacroSequenceElement suiteElement:suite)
								this.pressKey(suiteElement);
							break;
							
						default : break;
					}

					int sleepBetweenTwoKeys = Helper.getRandomNumberInRange(this.sleepBetweenTwoKeysStart, this.sleepBetweenTwoKeysEnd);
					logLN("Next key press in " + sleepBetweenTwoKeys + " ms [" + this.getFuturTime(sleepBetweenTwoKeys) + "]");
					super.sleep(sleepBetweenTwoKeys);
				}
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		log("fin de la macro");
	}
	
	private void pressKey(MacroSequenceElement element) throws InterruptedException
	{
		MacroSequenceElementType type = element.getType();
		int key = element.getKey();
		int pressedDelay = Helper.getRandomNumberInRange(element.getDelayStart(), element.getDelayStop());
		
		switch(type)
		{
			case KEYBOARD: 
				log(KeyEvent.getKeyText(key) + " pressed (" + pressedDelay + " ms) ");
				this.robot.keyPress(key);

				super.sleep(pressedDelay);

				logLN("released");
				this.robot.keyRelease(key);
				break;
				
			case MOUSE: 
				log(Helper.getMouseEventText(key) + " (" + pressedDelay + " ms) ");
				this.robot.mousePress(InputEvent.getMaskForButton(key));

				super.sleep(pressedDelay);

				logLN("released");
				this.robot.mouseRelease(InputEvent.getMaskForButton(key));
				break;
				
			default : break;
		}
	}

	private void logLN(String text)
	{
		this.log(text + "\n");
	}

	private void log(String text)
	{
		System.out.print(text);
		
		if(this.logger == null)
			return;
		
		if(this.logger != null)
			this.logger.setText(this.logger.getText() + text);
		this.logger.setCaretPosition(this.logger.getCaretPosition() + this.logger.getText().length());
	}

	private String getFuturTime(int ms)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, ms);
		return SDF.format(cal.getTime());
	}

	private MacroSequence sequence;
	private Robot robot;
	private int sleepBetweenTwoSequences;
	private int sleepBetweenTwoKeysStart;
	private int sleepBetweenTwoKeysEnd;
	private int sleepBetweenTwoSequencesRandom;
	private boolean checkShuffleSequence;
	private boolean exit;
	private JTextComponent logger;

	private static final Random random = new Random();
	private static final SimpleDateFormat SDF = new SimpleDateFormat("hh:mm:ss.SSS");
}
