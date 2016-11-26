from flask import Flask
from flask import request
from flask_sqlalchemy import SQLAlchemy
from os import urandom
from Crypto.Cipher import AES
from datetime import datetime
from proto.healthhelper import msg_pb2

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////tmp/health.db'
db = SQLAlchemy(app)
iv = b"IV_for_LeadroyaL"


class BodyInDB(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    time = db.Column(db.DateTime, default=datetime.now())
    xinlv = db.Column(db.Integer, unique=False)
    xueyang = db.Column(db.Integer, unique=False)
    deviceID = db.Column(db.DateTime, unique=False)

    def __init__(self, xinlv, xueyang, deviceID):
        self.xinlv = xinlv
        self.xueyang = xueyang
        self.deviceID = deviceID

    def __repr__(self):
        return '<id %id>' % self.id

    def __str__(self):
        return 'xl=%d, xy=%d\n' % (self.xinlv, self.xueyang)


def check_exists(needed, args):
    for need in needed:
        if need not in args:
            return False
    return True


def pkcs5_pad(data):
    if not isinstance(data, bytes):
        raise ValueError('first argument should be bytes')
    pad_len = AES.block_size - len(data) % AES.block_size
    padding = chr(pad_len).encode('ascii') * pad_len
    data += padding
    return data


def pkcs5_unpad(data):
    if not isinstance(data, bytes):
        raise ValueError('first argument should be bytes')
    pad_len = ord(data[-1:])
    data = data[:-pad_len]
    return data


@app.route('/')
def hello_world():
    # AES/CBC/PKCS5Padding
    aes_key = urandom(32)
    cipher = AES.AESCipher(aes_key, AES.MODE_CBC, iv)
    plain = pkcs5_pad("hello world!".encode())
    # print(plain)
    enc = cipher.encrypt(plain)
    # print(enc)
    cipher = AES.AESCipher(aes_key, AES.MODE_CBC, iv)
    dec = cipher.decrypt(enc)
    # print(dec)
    plain = pkcs5_unpad(dec)
    # print(plain)
    return plain


@app.route('/getToken', methods=['GET'])
def get_token():
    """
    # 1. generate
    # 2. insert
    :return:
    """
    needed_args = ['deviceID']
    if not check_exists(needed_args, request.args):
        return 'No device ID', 400
    device_id = request.args['deviceID']
    rand_key = urandom(32)
    # import redis
    # r = redis.StrictRedis(host='localhost', port=6379, db=0)
    # r.set(device_id, rand_key)
    return rand_key


@app.route('/uploadData', methods=['POST'])
def upload_data():
    """
    # 1. query for AESkey
    # 2. decrypt
    # 3. parse
    # 4. insert
    :return:
    """
    needed_args = ['deviceID', 'data']
    if not check_exists(needed_args, request.form):
        return 'No device ID or input data', 400
    device_id = request.form['deviceID']
    data = request.form['data']
    enc_data = bytes.fromhex(data)
    import redis
    r = redis.StrictRedis(host='localhost', port=6379, db=0)
    aes_key = r.get(device_id)
    cipher = AES.AESCipher(aes_key, AES.MODE_CBC, iv)
    dec = cipher.decrypt(enc_data)
    plain = pkcs5_unpad(dec)
    body = msg_pb2.Body()
    body.ParseFromString(plain)
    xinlv = body.xinlv
    xueyang = body.xueyang
    b = BodyInDB(xinlv, xueyang, device_id)
    db.session.add(b)
    db.session.commit()


@app.route('/show', methods=['POST'])
def get_data_by_client():
    # 1. query
    # 2. return template
    needed_args = ['deviceID']
    if not check_exists(needed_args, request.form):
        return 'No device ID', 400
    device_id = request.form['deviceID']
    bodies = BodyInDB.query.filter_by(deviceID=device_id).all()
    res = device_id + '\n'
    for body in bodies:
        res += body
    return res


@app.route('/showAll')
def get_all_data():
    # 1. select
    # 2. show
    res = ""
    bodies = BodyInDB.query.all()
    for body in bodies:
        res += body
    return res


if __name__ == '__main__':
    app.run()
