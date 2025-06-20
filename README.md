# 1. Test through JMeter
* Step 1: Turn on JMeter
* Step 2: Open ISO8385 Sampler and fill field with value there (if don't have ISO8385 plugin you should install it from Plugin Manager)
* Step 3: Open ISO8385 Message Component and do the same like step 2
* Step 4: Open ISO8385 Connection Configuration and select ASCIIChannel, transmit path of your custom bitmap xml file, fill localhost at hostname, port 10000, Reuse Connection True, Maximum Connections 5 and Connection Selection is Last Connected.
* Step 5: Open View Result Tree (Test Plan > Listener > View Result Tree) to see the messages are sent or not.
* Step 6: Open Aggregate Report (Test Plan > Listener > Aggregate Report) to see the information of number of samples, error percentage,...
* Step 7: Set up Number of Threads (users), Loop Count and Duration (if need).
* Step 8: Run the program.
* Step 9: Click run button and see the result from View Results Tree.
* Step 10: Go q2.log to see log information of ISO8385 message.