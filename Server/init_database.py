from health import db
from health import BodyInDB

db.create_all()

data1 = BodyInDB(1, 11)
data2 = BodyInDB(2, 22)

db.session.add(data1)
db.session.add(data2)

db.session.commit()
