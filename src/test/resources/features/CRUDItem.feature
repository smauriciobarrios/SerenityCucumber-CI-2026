Feature: ItemApiRest
  Scenario: CRUD Item Api Rest
   Given Sergio tiene acceso a Todo.ly
   #create
   When he registra un item con nombre "Serenity Cucumber Screenplay"
   Then he revisa que el item queda registrado "Serenity Cucumber Screenplay"
   #update
   When he actualiza el nombre del item a "Serenity Cucumber Screenplay Updated"
   Then he revisa que el item queda registrado "Serenity Cucumber Screenplay Updated"
   #read
   When he consulta el item
   Then he visualiza la informacion del item
   #delete
   When he elimina el item
   Then he revisa que el item ya no se encuentra disponible
