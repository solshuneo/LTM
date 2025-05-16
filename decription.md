server
    - 1 or n clients were connected
    - list of task to do, finished, processing, waiting
    - receive a new task:
        - append to task to do with id-client and picture
    - processing list of task to do, one by one
    - after finished one task, add label finished, new task is processing, and res is waiting for.
    - send result of above finished task to id-client with result among.
client:
    - 1 or n clients connect to server
    - client has a list of tasks
    - declare: list of task, finished or not send or waiting
    - send a task if not send yet, once
        + id-client
        + picture
        -> receive a waiting signal from server and mark this task to waiting label
    - view display list of task, name, file_result (optional) with their label
        finished: green
        not send: red
        waiting : yellow
    