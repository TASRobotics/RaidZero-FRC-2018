const filename = '/home/lvuser/data.csv';
const columnCount = 2;

const clip = require('clipboardy');
const conn = new require('ssh2').Client();

console.log('Reading data from clipboard...');

const data = [];
outer:
for (const line of clip.readSync().trim().split('\n')) {
    const nums = line.trim().split('\t');
    for (const num of nums) {
        if (num === '?') {
            break outer;
        }
        if (Number.isNaN(parseFloat(num))) {
            throw new Error(`${num} is not a number`);
        }
    }
    if (nums.length !== columnCount) {
        throw new Error(
            `Data contains ${nums.length} column${nums.length === 1 ? '' : 's'} but should have ${columnCount}`);
    }
    data.push(nums.join());
}

console.log('Connecting to robot...');

conn.on('ready', () => {
    console.log('Initializing SFTP...');
    conn.sftp((err, sftp) => {
        if (err) throw err;
        console.log('Opening file...');
        sftp.open(filename, 'w', (err, handle) => {
            if (err) throw err;
            const buffer = Buffer.from(data.join('\n'));
            console.log('Writing data...');
            sftp.write(handle, buffer, 0, buffer.length, 0, err => {
                if (err) throw err;
                console.log('Closing file...');
                sftp.close(handle, err => {
                    if (err) throw err;
                    conn.end();
                    console.log('Successfully transferred data to robot');
                });
            });
        });
    });
}).connect({
    host: 'roboRIO-4253-FRC.local',
    port: 22,
    username: 'lvuser',
    password: ''
});
