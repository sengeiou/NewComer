from com.android.monkeyrunner import MonkeyRunner,MonkeyDevice,MonkeyImage

device=MonkeyRunner.waitForConnection()
device.wake()
MonkeyRunner.sleep(1)
result=device.takeSnapshot()
result.writeToFile('/home/hjc/img/img05.png','png')
