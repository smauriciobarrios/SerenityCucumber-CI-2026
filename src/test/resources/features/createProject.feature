Feature: ProjectApiRest
  Scenario: Crear Proyecto por API

    # el actor: se puede usar un pronombre he/she
    Given Carlos is an user on todo.ly
    When he create a project with "Serenity Cucumber"
    Then he should see the project "Serenity Cucumber"