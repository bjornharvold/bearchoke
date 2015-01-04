describe 'fetcher', ->
  Given -> @subject = requireSubject('lib/fetcher')
  When -> @subject.fetch()
  Then ->
